package per.wilson.distributed.config;

import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
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
public class RedisConfig {
    @Resource
    private Environment environment;

    private List<String> hosts;

    private List<Integer> ports;

    private int timeout;

    private int maxAttempt;


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.jedis.pool.config")
    public GenericObjectPoolConfig poolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public RedisPassword redisPassword() {
        return RedisPassword.of(environment.getProperty("spring.redis.password"));
    }

    @Bean
    public Set<HostAndPort> nodes() {
        Set<HostAndPort> nodes = new HashSet<>();
        IntStream.rangeClosed(0, nodes.size())
                .forEach(e -> nodes.add(new HostAndPort(hosts.get(e), ports.get(e))));
        return nodes;
    }

    @Bean
    public JedisCluster jedisCluster(Set<HostAndPort> nodes,GenericObjectPoolConfig poolConfig) {
        return new JedisCluster(nodes,timeout,maxAttempt,poolConfig);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redis.is-init", havingValue = "true")
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisStandaloneConfiguration redisStandaloneConfiguration(RedisPassword redisPassword) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setPassword(redisPassword);
        return configuration;
    }

    @Bean
    @ConditionalOnBean(RedisStandaloneConfiguration.class)
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @ConditionalOnBean(JedisConnectionFactory.class)
    public CacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory) {
        return RedisCacheManager.create(jedisConnectionFactory);
    }

}
