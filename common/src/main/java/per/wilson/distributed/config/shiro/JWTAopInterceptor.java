package per.wilson.distributed.config.shiro;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.wilson.distributed.config.shiro.interceptor.JWTPermissionInterceptor;
import per.wilson.distributed.config.shiro.interceptor.JWTRoleInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JWTAopInterceptor
 *
 * @author Wilson
 * @date 18-7-25
 */
public class JWTAopInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor implements MethodInterceptor {

    public JWTAopInterceptor() {
        List<AuthorizingAnnotationMethodInterceptor> interceptors = new ArrayList<>(5);
        //use a Spring-specific Annotation resolver - Spring's AnnotationUtils is nicer than the
        //raw JDK resolution process.
        AnnotationResolver resolver = new SpringAnnotationResolver();
        //we can re-use the same resolver instance - it does not retain state:
        interceptors.add(new JWTPermissionInterceptor(resolver));
        interceptors.add(new JWTRoleInterceptor(resolver));
        setMethodInterceptors(interceptors);
    }

}
