package per.wilson.distributed.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * DataSourceProperties
 *
 * @author Wilson
 * @date 18-7-10
 */
@Configuration
@EnableAutoConfiguration
@RefreshScope
public class DataSourceConfig {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "jdbc")
    public DruidDataSource dataSource() {
        return new DruidDataSource();
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
