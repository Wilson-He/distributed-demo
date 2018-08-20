package per.wilson.distributed.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import per.wilson.distributed.config.JedisClusterUtil;

import javax.annotation.Resource;

/**
 * UserServiceTest
 *
 * @author Wilson
 * @date 18-8-18
 */
@SpringBootTest
@SpringJUnitConfig
class UserServiceTest {
    @Resource
    private UserService userService;
    @Resource
    private JedisClusterUtil jedisClusterUtil;

    @Test
    void listUser() {
        System.out.println(userService.getById(1111L));
        System.out.println("1:" + jedisClusterUtil.get("1"));
        System.out.println("execute second");
        System.out.println(userService.getById(1111L));
        System.out.println("keys:" + jedisClusterUtil.keys("*"));
    }
}
