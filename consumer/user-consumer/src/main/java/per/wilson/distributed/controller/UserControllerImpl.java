package per.wilson.distributed.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.wilson.distributed.config.shiro.JWTToken;
import per.wilson.distributed.model.entity.User;
import per.wilson.distributed.service.UserService;
import per.wilson.distributed.utils.JWTUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.time.Clock;
import java.time.Instant;
import java.util.Enumeration;
import java.util.List;

/**
 * UserController
 *
 * @author Wilson
 * @date 18-7-11
 */
@RestController
@Api
public class UserControllerImpl implements UserController {

    @Reference
    private UserService userService;
    @Resource
    private SecurityManager securityManager;
    @Resource
    private HttpServletResponse response;
    @Resource
    private HttpServletRequest request;

    @Override
    public List<User> list(@RequestParam @Max(3) int page) {
        return userService.listByWrapper(null);
    }

    @Override
    public Object string(@RequestParam @Pattern(regexp = "AAA") String name,
                         @RequestParam @Pattern(regexp = "BBB") String password) {
        return name + password;
    }

    @Override
    public Object header() {
        Enumeration<String> headers = request.getHeaderNames();
        StringBuilder builder = new StringBuilder();
        if (headers.hasMoreElements()) {
            String next = headers.nextElement();
            builder.append(next)
                    .append(":")
                    .append(request.getHeader(next))
                    .append("\n");
        }
        return builder.toString();
    }

    @Override
    public Integer number(@RequestParam @Max(3) Integer number) {
        return userService.number(number);
    }

    @Override
    public Object getToken(@RequestParam(required = false) String account, @RequestParam(required = false) List<String> roles,
                           @RequestParam(required = false) String password) {
        return Jwts.builder()
                .signWith(JWTUtils.signatureAlgorithm, JWTUtils.key)
                .claim("username", account)
                .claim("password", password)
                .claim("roles", roles)
                .compact();
    }

    @Override
    public Object checkToken(@RequestParam String token) {
        return JWTUtils.parseJWT(token);
    }

    @Override
    public Object login(@RequestParam String username, @RequestParam String password) {
        String token = JWTUtils.createToken(username, password);
        JWTToken jwtToken = new JWTToken(token);
        Subject subject = SecurityUtils.getSubject();
        subject.login(jwtToken);
        response.addHeader(JWTToken.AUTHORIZATION, token);
        return "success";
    }

    @Override
    public boolean insert(@Validated @RequestBody User user) {
        Instant now = Instant.now(Clock.systemDefaultZone());
        user.setCreateTime(now);
        System.out.println("now:" + now);
        return userService.insert(user);
    }

    @Override
    public Object role() {
        return "You have role";
    }

    @Override
    public Object permission() {
        return "You have permission";
    }

    @Override
    public Object other() {
        return "You have other";
    }
}
