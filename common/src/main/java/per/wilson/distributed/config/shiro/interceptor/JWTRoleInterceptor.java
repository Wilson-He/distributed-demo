package per.wilson.distributed.config.shiro.interceptor;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

/**
 * JWTRoleAnnotationMethodInterceptor
 *
 * @author Wilson
 * @date 18-7-25
 */
public class JWTRoleInterceptor extends AuthorizingAnnotationMethodInterceptor {

    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            ((JWTRoleHandler) getHandler()).assertAuthorized(getAnnotation(mi));
        } catch (AuthorizationException ae) {
            if (ae.getCause() == null)
                ae.initCause(new AuthorizationException("JWTRoleInterceptor not authorized to invoke method: " + mi.getMethod()));
            throw ae;
        }
    }

    /**
     * @param resolver
     * @since 1.1
     */
    public JWTRoleInterceptor(AnnotationResolver resolver) {
        super(new JWTRoleHandler(), resolver);
    }
}
