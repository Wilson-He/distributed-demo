package per.wilson.distributed.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import per.wilson.distributed.entity.model.User;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JedisClusterTest
 *
 * @author Wilson
 * @date 18-8-13
 */
@SpringBootTest(classes = UserProviderConfig.class)
@SpringJUnitConfig
class JedisClusterTest {
    @Resource
    private JedisClusterUtil jedisClusterUtil;

    private static final String STRING_KEY = "name";
    private static final String OBJECT_KEY = "userWilson";

    @Test
    void setStringTest() {
        assertEquals(jedisClusterUtil.put(STRING_KEY, "Wilson"), "OK");
    }

    @Test
    void getStringTest() {
        assertEquals(jedisClusterUtil.get(STRING_KEY), "Wilson");
    }

     @Test
    void del() {
         assertTrue(jedisClusterUtil.del(STRING_KEY));
    }

    @Test
    void setObjectTest() {
        User user = new User();
        user.setUsername("Wilson");
        user.setPassword("000000");
        assertEquals(jedisClusterUtil.putObject(OBJECT_KEY, user), "OK");
    }

    @Test
    void getObjectTest() {
        assertEquals(jedisClusterUtil.getObject(OBJECT_KEY, User.class).getPassword(), "000000");
    }

}
