package per.wilson.distributed.config.shiro;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.config.AbstractShiroAnnotationProcessorConfiguration;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.Filter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ShiroConfig
 *
 * @author Wilson
 * @date 18-7-5
 */
@org.springframework.context.annotation.Configuration
public class ShiroConfig extends AbstractShiroAnnotationProcessorConfiguration{
    /**
     * shiro过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error.html");
        //添加表单拦截器
       /* Map filters = shiroFilterFactoryBean.getFilters();
        filters.put("formFilter", formFilter);*/
        //设置过滤链
        Properties properties = new Properties();
        Map<String, Filter> map = new LinkedHashMap<>();
        map.put("jwt", new JWTFilter());
        try {
            InputStream inputStream = new ClassPathResource("shiroMapFilter.properties").getInputStream();
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("shiroMapFilter.properties文件不存在");
        }
        LinkedHashMap<String, String> filterChain = new LinkedHashMap(properties);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChain);
        shiroFilterFactoryBean.setFilters(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    @Override
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        JWTAuthorizationAdvisor authorizationAdvisor = new JWTAuthorizationAdvisor();
        authorizationAdvisor.setSecurityManager(securityManager);
        return authorizationAdvisor;
    }

    @Bean
    public SecurityManager securityManager(Realm customRealm, CacheManager cacheManager,
                                           RememberMeManager rememberMeManager, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * shiro生命周期处理器
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,
     * 并在必要时进行安全逻辑验证 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return sessionDAO;
    }

    /**
     * 凭证匹配器
     *
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(CacheManager shiroCacgeManager) {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(1);        //哈希加密次数，此处为进行2次MD5加密
        credentialsMatcher.setHashAlgorithmName("md5"); //设置加密方式为MD5
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


    /**
     * 开启spring shiro aop注解支持.使用代理方式需开启代码支持
     *
     * @param securityManager
     * @return
     */
 /*   @Bean
    @Override
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }*/

    @Bean
    public Cookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public Cookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    @Bean
    public RememberMeManager rememberMeManager(Cookie rememberMeCookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        //cipherKey是加密rememberMe Cookie的密钥,默认AES算法
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        rememberMeManager.setCookie(rememberMeCookie);
        return rememberMeManager;
    }


    /**
     * 自定义身份验证Realm
     *
     * @param credentialsMatcher 凭证匹配器
     * @return
     */
    @Bean
    public AuthorizingRealm customRealm(CredentialsMatcher credentialsMatcher) {
        AuthorizingRealm realm = new UserRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        realm.setCachingEnabled(true);          //启用缓存，默认false
        /*realm.setAuthorizationCachingEnabled(true);//启用授权缓存，默认false
        realm.setAuthorizationCacheName("authorizationCache");
        realm.setAuthenticationCachingEnabled(true);//启用身份验证缓存
        realm.setAuthenticationCacheName("authenticationCache");//设置缓存名称*/
        return realm;
    }

    /**
     * 会话验证调度器，间隔检测用户是否退出
     *
     * @param sessionManager 会话管理器
     * @return
     */
    @Bean
    public SessionValidationScheduler sessionValidationScheduler(SessionManager sessionManager) {
        QuartzSessionValidationScheduler sessionValidationScheduler = new QuartzSessionValidationScheduler();
        sessionValidationScheduler.setSessionValidationInterval(1800000);   //调度时间间隔为半小时(单位:毫秒)
        sessionValidationScheduler.setSessionManager((ValidatingSessionManager) sessionManager);
        return sessionValidationScheduler;
    }

    /**
     * 会话管理器
     *
     * @param cacheManager 缓存管理器
     * @param sessionDAO   会话DAO
     * @return
     */
    @Bean
    public SessionManager sessionManager(CacheManager cacheManager, SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setCacheManager(cacheManager);
        return sessionManager;
    }

    @Bean("shiroCacgeManager")
    public CacheManager cacheManager() {
        EhCacheManager ehcacheManager = new EhCacheManager();
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance();
        Configuration configuration = new Configuration();
        configuration.addCache(sessionCacheConfiguration());
        configuration.addCache(defaultCacheConfiguration());
        ehcacheManager.setCacheManager(cacheManager);
        return ehcacheManager;
    }

    private CacheConfiguration sessionCacheConfiguration() {
        CacheConfiguration configuration = new CacheConfiguration("shiro-activeSessionCache", 10000);
        configuration.setEternal(true);
        configuration.setTimeToLiveSeconds(0);
        configuration.setTimeToIdleSeconds(0);
        configuration.setDiskExpiryThreadIntervalSeconds(600);
        configuration.persistence(new PersistenceConfiguration()
                .strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP));
        return configuration;
    }

    private CacheConfiguration defaultCacheConfiguration() {
        CacheConfiguration configuration = new CacheConfiguration("defaultCache", 10000);
        configuration.setEternal(true);
        configuration.setTimeToLiveSeconds(0);
        configuration.setTimeToIdleSeconds(0);
        configuration.setDiskExpiryThreadIntervalSeconds(600);
        configuration.persistence(new PersistenceConfiguration()
                .strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP));
        return configuration;
    }

}
