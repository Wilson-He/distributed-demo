package per.wilson.distributed.config;

import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * RedisConfig
 *
 * @author Wilson
 * @date 18-8-9
 */
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties("spring.redis.cluster.nodes")
@Setter
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Resource
    private Environment environment;

    private List<String> hosts;

    private List<Integer> ports;

    private int timeout;

    private int maxAttempt;


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public RedisPassword redisPassword() {
        return RedisPassword.of(environment.getProperty("spring.redis.password"));
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        return new JedisConnectionFactory(jedisPoolConfig);
    }

    @Bean
    public CacheManager cacheManager(JedisClusterUtil jedisClusterUtil) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new RedisClusterCache(true,jedisClusterUtil,"cacheManager")));
        return cacheManager;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            String key = new StringBuilder()
                    .append(o.getClass().getName())
                    .append(method.getName())
                    .append(Arrays.deepToString(objects))
                    .toString();
            return key;
        };

    }

    /**
     * 选择jedis配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.is-init", havingValue = "false")
    public Set<HostAndPort> nodes() {
        Set<HostAndPort> nodes = new HashSet<>();
        IntStream.rangeClosed(0, nodes.size())
                .forEach(e -> nodes.add(new HostAndPort(hosts.get(e), ports.get(e))));
        return nodes;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redis.is-init", havingValue = "false")
    public JedisCluster jedisCluster(Set<HostAndPort> nodes, JedisPoolConfig jedisPoolConfig) {
        return new JedisCluster(nodes, timeout, maxAttempt, jedisPoolConfig);
    }

    /**
     * 选择spring-data-redis配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.is-init", havingValue = "true")
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisStandaloneConfiguration redisStandaloneConfiguration(RedisPassword redisPassword) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setPassword(redisPassword);
        return configuration;
    }
}
