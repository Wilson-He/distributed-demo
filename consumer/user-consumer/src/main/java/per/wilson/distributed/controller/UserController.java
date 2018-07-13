package per.wilson.distributed.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import per.wilson.distributed.model.entity.User;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * UserControllerImpl
 *
 * @author Wilson
 * @date 18-7-11
 */
@Validated
@RequestMapping("/")
public interface UserController {
    @GetMapping("list")
    List<User> list(@RequestParam @Max(3) int page);

    @GetMapping("string")
    Object string(@RequestParam @Pattern(regexp = "AAA") String name,
                  @RequestParam @Pattern(regexp = "BBB") String password);

    @GetMapping("number")
    Integer number(@RequestParam @Max(3) Integer number);

    @PostMapping("insert")
    boolean insert(@RequestBody User user);
}
