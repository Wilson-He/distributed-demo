package per.wilson.distributed.config.shiro.interceptor;

import io.jsonwebtoken.lang.Collections;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import per.wilson.distributed.config.shiro.JWTToken;
import per.wilson.distributed.utils.JWTUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JWTAuthorizingAnnotationHandler
 *
 * @author Wilson
 * @date 18-7-25
 */
public class JWTRoleHandler extends AuthorizingAnnotationHandler {

    private final static Logger log = LoggerFactory.getLogger(JWTRoleHandler.class);

    JWTRoleHandler() {
        super(RequiresRoles.class);
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (!(a instanceof RequiresRoles)) {
            return;
        }
        log.info("JWTRoleHandler assertAuthorized");
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader(JWTToken.AUTHORIZATION);
        if (token == null) {
            throw new AuthorizationException();
        }
        List<String> tokenRoles = JWTUtils.parseToBody(token).get("roles", ArrayList.class);
        if (tokenRoles == null) {
            throw new AuthorizationException();
        }
        RequiresRoles rrAnnotation = (RequiresRoles) a;
        String[] roles = rrAnnotation.value();
        if (roles.length == 1 && tokenRoles.contains(roles[0])) {
            return;
        }
        List<String> roleList = Arrays.asList(roles);
        if (Logical.AND.equals(rrAnnotation.logical()) && tokenRoles.containsAll(roleList)) {
            return;
        }
        if (Logical.OR.equals(rrAnnotation.logical()) && Collections.containsAny(tokenRoles, roleList)) {
            return;
        }
        throw new AuthorizationException();
    }

}
