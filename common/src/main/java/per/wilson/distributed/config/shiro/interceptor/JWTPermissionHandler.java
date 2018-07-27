package per.wilson.distributed.config.shiro.interceptor;

import io.jsonwebtoken.lang.Collections;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import per.wilson.distributed.config.shiro.JWTToken;
import per.wilson.distributed.utils.JWTUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JWTPermissionHandler
 *
 * @author Wilson
 * @date 18-7-26
 */
public class JWTPermissionHandler extends AuthorizingAnnotationHandler {

    public JWTPermissionHandler() {
        super(RequiresPermissions.class);
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (!(a instanceof RequiresPermissions)) {
            return;
        }
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader(JWTToken.AUTHORIZATION);
        if (token == null) {
            throw new UnauthorizedException();
        }
        ArrayList tokenPermissions = JWTUtils.parseToBody(token)
                .get("permissions", ArrayList.class);
        if (tokenPermissions == null) {
            throw new UnauthorizedException();
        }
        RequiresPermissions rrAnnotation = (RequiresPermissions) a;
        String[] permissions = rrAnnotation.value();
        if (permissions.length == 1 && tokenPermissions.contains(permissions[0])) {
            return;
        }
        List<String> roleList = Arrays.asList(permissions);
        if (Logical.AND.equals(rrAnnotation.logical()) && tokenPermissions.containsAll(roleList)) {
            return;
        }
        if (Logical.OR.equals(rrAnnotation.logical()) && Collections.containsAny(tokenPermissions, roleList)) {
            return;
        }
        throw new UnauthorizedException();
    }
}
