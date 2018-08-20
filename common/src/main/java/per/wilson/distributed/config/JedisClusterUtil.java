package per.wilson.distributed.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

    private final Long DEFAULT_EXPIRED = 60 * 30L;

    public String put(String key, String value) {
        return put(key, value, DEFAULT_EXPIRED);
    }

   public String put(String key, String value,Long expiredSecond) {
        return jedisCluster.set(key, value, "NX", "EX", expiredSecond);
    }

    public String putIfAbsent(String key, Object value) {
        if (exists(key)) {
            return null;
        }
        return value instanceof String ? put(key, (String) value,DEFAULT_EXPIRED) : putObject(key, value);
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

    public void clear(){
        Map<String, JedisPool> poolMap = jedisCluster.getClusterNodes();
        Set<String> treeSet = new TreeSet<>();
        poolMap.forEach((k, v) -> {
            try (Jedis jedis = v.getResource()) {
                treeSet.addAll(jedis.keys("*"));
            }
        });
        jedisCluster.del((String[]) treeSet.toArray());
    }

    public Set<String> keys(String pattern) {
        Map<String, JedisPool> poolMap = jedisCluster.getClusterNodes();
        Set<String> treeSet = new TreeSet<>();
        poolMap.forEach((k, v) -> {
            try (Jedis jedis = v.getResource()) {
                treeSet.addAll(jedis.keys(pattern));
            }
        });
        return treeSet;
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

    public String putObject(String key, Object object) {
        return put(key, JSONObject.toJSONString(object));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return JSONObject.parseObject(get(key), clazz);
    }

    private boolean isKeyEmpty(String key) {
        return key == null || key.length() == 0;
    }

}
