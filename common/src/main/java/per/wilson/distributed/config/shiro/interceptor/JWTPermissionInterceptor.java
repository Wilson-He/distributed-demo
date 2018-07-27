package per.wilson.distributed.config.shiro.interceptor;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

/**
 * JWTPermissionInterceptor
 *
 * @author Wilson
 * @date 18-7-26
 */
public class JWTPermissionInterceptor extends AuthorizingAnnotationMethodInterceptor {
    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            ((JWTPermissionHandler) getHandler()).assertAuthorized(getAnnotation(mi));
        } catch (AuthorizationException ae) {
            // Annotation handler doesn't know why it was called, so add the information here if possible.
            // Don't wrap the exception here since we don't want to mask the specific exception, such as
            // UnauthenticatedException etc.
            if (ae.getCause() == null)
                ae.initCause(new AuthorizationException("JWTPermissionInterceptor not authorized to invoke method: " + mi.getMethod()));
            throw ae;
        }
    }
    /**
     * @param resolver
     * @since 1.1
     */
    public JWTPermissionInterceptor(AnnotationResolver resolver) {
        super(new JWTPermissionHandler(), resolver);
    }
}
