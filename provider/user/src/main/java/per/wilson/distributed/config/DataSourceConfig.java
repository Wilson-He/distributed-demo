package per.wilson.distributed.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import per.wilson.distributed.utils.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * DataSourceProperties
 *
 * @author Wilson
 * @date 18-7-10
 */
@Configuration
@ConfigurationProperties(prefix = "jdbc")
@PropertySource("classpath:jdbc.properties")
@ToString
@Setter
@Getter
public class DataSourceConfig {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private String filters;
    private Integer maxActive;
    private Integer initialSize;
    private Long maxWait;
    private Integer minIdle;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean poolPreparedStatements;
    private Integer maxOpenPreparedStatements;
    private Boolean asyncInit;

    @PostConstruct
    public void init() {
        System.err.println("DataSourceConfig:" + toString());
    }

    @Bean
    public DataSource dataSource() {
        System.err.println("DataSource init:" + toString());
        return ObjectUtils.copyProperties(this, new DruidDataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {
        Properties properties = new Properties();
        properties.put("*", "PROPAGATION_REQUIRED");
        return new TransactionInterceptor(transactionManager, properties);
    }
}
