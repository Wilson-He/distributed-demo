package per.wilson.distributed.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.wilson.distributed.model.entity.User;
import per.wilson.distributed.service.UserService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

/**
 * UserController
 *
 * @author Wilson
 * @date 18-7-11
 */
@RestController
@RequestMapping("/")
@Validated
public class UserControllerImpl implements UserController {

    @Reference
    private UserService userService;

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
    public Integer number(@RequestParam @Max(3) Integer number) {
        return userService.number(number);
    }

    @Override
    public boolean insert(@Validated @RequestBody User user) {
        Instant now = Instant.now(Clock.systemDefaultZone());
        user.setCreateTime(now);
        System.out.println("now:" + now);
        return userService.insert(user);
    }
}
