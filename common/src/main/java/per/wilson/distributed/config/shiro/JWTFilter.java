package per.wilson.distributed.config.shiro;

import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.wilson.distributed.utils.JWTUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 使用JWT进行HTTP Basic Authentication、基础权限校验与JWT发放
 *
 * @author Wilson
 * @date 18-7-19
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * The static logger available to this class only
     */
    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String jwt = getAuthzHeader(request);
        AuthenticationToken token = new JWTToken(jwt);
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * 登录成功后添加JWT Token到Header
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        JWTToken jwtToken = (JWTToken) token;
        HttpServletResponse servletResponse = WebUtils.toHttp(response);
        servletResponse.addHeader(AUTHORIZATION_HEADER, JWTUtils.createJWT(jwtToken.getPrincipal()));
        return super.onLoginSuccess(token, subject, request, response);
    }

    /**
     * 覆写判断是否尝试登录请求
     * 将原方法Header Authorization带前缀BASIC才为true改为只要Header含key=Authorization则为true
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String tokenHeader = WebUtils.toHttp(request).getHeader(AUTHORIZATION_HEADER);
        return StringUtils.isNotBlank(tokenHeader) && JWTUtils.parseToBody(tokenHeader).get("password") != null;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String httpMethod = httpRequest.getMethod();
        // Check whether the current request's method requires authentication.
        // If no methods have been configured, then all of them require auth,
        // otherwise only the declared ones need authentication.
        Set<String> methods = httpMethodsFromOptions((String[]) mappedValue);
        boolean authcRequired = methods.size() == 0;
        for (String m : methods) {
            if (httpMethod.toUpperCase(Locale.ENGLISH).equals(m)) { // list of methods is in upper case
                authcRequired = true;
                break;
            }
        }
        return !authcRequired || !isLoginRequest(request, response) && isPermissive(mappedValue);
    }

    @Override
    protected boolean isPermissive(Object mappedValue) {
        if (mappedValue != null) {
            String[] values = (String[]) mappedValue;
            return Arrays.binarySearch(values, PERMISSIVE) >= 0;
        }
        return true;
    }

    private Set<String> httpMethodsFromOptions(String[] options) {
        Set<String> methods = new HashSet<String>();
        if (options != null) {
            for (String option : options) {
                if (!option.equalsIgnoreCase(PERMISSIVE)) {
                    methods.add(option.toUpperCase(Locale.ENGLISH));
                }
            }
        }
        return methods;
    }


    @Override
    protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("username", username);
        return new JWTToken(Jwts.builder()
                .addClaims(claims)
                .setIssuer(host)
                .compact());
    }
}
