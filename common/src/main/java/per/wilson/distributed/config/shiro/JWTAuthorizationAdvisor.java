package per.wilson.distributed.config.shiro;

import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * JWTAuthorizationAdvisor
 *
 * @author Wilson
 * @date 18-7-25
 */
public class JWTAuthorizationAdvisor extends AuthorizationAttributeSourceAdvisor {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationAdvisor.class);

    private static final Class[] AUTHZ_ANNOTATION_CLASSES =
            new Class[]{
                    RequiresPermissions.class, RequiresRoles.class,
                    RequiresUser.class, RequiresGuest.class, RequiresAuthentication.class
            };

    private SecurityManager securityManager = null;

    /**
     * Create a new AuthorizationAttributeSourceAdvisor.
     */
    public JWTAuthorizationAdvisor() {
        setAdvice(new JWTAopInterceptor());
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(org.apache.shiro.mgt.SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Returns <tt>true</tt> if the method or the class has any Shiro annotations, false otherwise.
     * The annotations inspected are:
     * <ul>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresRoles RequiresRoles}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions}</li>
     * </ul>
     *
     * @param method      the method to check for a Shiro annotation
     * @param targetClass the class potentially declaring Shiro annotations
     * @return <tt>true</tt> if the method has a Shiro annotation, false otherwise.
     * @see org.springframework.aop.MethodMatcher#matches(java.lang.reflect.Method, Class)
     */
    public boolean matches(Method method, Class targetClass) {
        Method m = method;

        if (isAuthzAnnotationPresent(m)) {
            return true;
        }

        //The 'method' parameter could be from an interface that doesn't have the annotation.
        //Check to see if the implementation has it.
        try {
            m = targetClass.getMethod(m.getName(), m.getParameterTypes());
            return isAuthzAnnotationPresent(m) || isAuthzAnnotationPresent(targetClass);
        } catch (NoSuchMethodException ignored) {
            //default return value is false.  If we can't find the method, then obviously
            //there is no annotation, so just use the default return value.
        }

        return false;
    }

    private boolean isAuthzAnnotationPresent(Class<?> targetClazz) {
        for (Class annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(targetClazz, annClass);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthzAnnotationPresent(Method method) {
        for (Class annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

}
