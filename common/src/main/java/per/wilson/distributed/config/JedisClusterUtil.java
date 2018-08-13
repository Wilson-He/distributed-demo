package per.wilson.distributed.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * JedisClusterUtil
 *
 * @author Wilson
 * @date 18-8-13
 */
@Component
public class JedisClusterUtil {
    @Resource
    private JedisCluster jedisCluster;

    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    public String get(String key) {
        return jedisCluster.get(key);
    }

    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    public boolean del(String key) {
        return jedisCluster.del(key) == 1L;
    }

    /**
     * time to live 获取key剩余的存活时间
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    public Long expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    public <T> String setObject(String key, T object) {
        return set(key, JSONObject.toJSONString(object));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return JSONObject.parseObject(get(key), clazz);
    }

    private boolean isKeyEmpty(String key) {
        return key == null || key.length() == 0;
    }

}
