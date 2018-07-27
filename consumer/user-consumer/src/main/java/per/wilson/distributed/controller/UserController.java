package per.wilson.distributed.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
@Api
public interface UserController {
    @GetMapping("list")
    @ApiOperation("list")
    List<User> list(@RequestParam @Max(3) int page);

    @GetMapping("string")
    @ApiOperation("string")
    Object string(@RequestParam @Pattern(regexp = "AAA") String name,
                  @RequestParam @Pattern(regexp = "BBB") String password);

    @GetMapping("header")
    @ApiOperation("header")
    Object header();

    @GetMapping("number")
    Integer number(@RequestParam @Max(3) Integer number);

    @GetMapping("getToken")
    @ApiOperation("getToken")
    Object getToken(String account, List<String> roles, String password);

    @GetMapping("checkToken")
    @ApiOperation("checkToken")
    Object checkToken(@RequestParam String token);

    @GetMapping("login")
    @ApiOperation("login")
    Object login(@RequestParam String username, @RequestParam String password);

    @PostMapping("insert")
    @ApiOperation("insert")
    boolean insert(@Validated @RequestBody User user);


    @GetMapping("role")
    @RequiresRoles("role")
    Object role();

    @GetMapping("permission")
    @RequiresPermissions("permission")
    Object permission();

    @GetMapping("other")
    @RequiresPermissions("other")
    Object other();
}
