package per.wilson.distributed.controller;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * BusinessController
 *
 * @author Wilson
 * @date 18-7-23
 */
@RequestMapping("/")
@Api
public interface BusinessController {
    @GetMapping("admin")
    @RequiresPermissions("ADMIN")
    Object admin();

    @GetMapping("manager")
    @RequiresPermissions("Manager")
    Object manager();
}
