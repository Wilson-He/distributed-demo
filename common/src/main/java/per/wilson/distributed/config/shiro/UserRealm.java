package per.wilson.distributed.config.shiro;

/**
 * Created by mac on 2017/8/30.
 */

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.wilson.distributed.model.entity.User;
import per.wilson.distributed.service.UserService;

import java.util.List;

public class UserRealm extends AuthorizingRealm {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<Object> list = principalCollection.asList();
        if (list != null && !list.isEmpty()) {
            //赋权
           /* authorizationInfo.addRole(manager.getRole());
            List<String> permissionList = (List<String>) ManagerVar.ROLE_PERMISSIONS_MAP.get(manager.getRole());
            Set<String> permissions = new HashSet<>(permissionList);
            authorizationInfo.addStringPermissions(permissions);*/
            return authorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JWTToken token = (JWTToken) authenticationToken;
        String account = token.getPrincipal();
        if (token.getCredentials() != null) {
            User user = userService.getByField("username", account);
            if (user == null) {
                throw new UnknownAccountException();
            }
            return new SimpleAuthenticationInfo(user, token, getName());
        } else {
            //密码不符合传输规则返回错误
            return null;
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token) || token instanceof JWTToken;
    }

    @Override
    public Class getAuthenticationTokenClass() {
        return JWTToken.class;
    }
}
