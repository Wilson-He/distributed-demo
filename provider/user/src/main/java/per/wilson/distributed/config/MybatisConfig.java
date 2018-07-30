package per.wilson.distributed.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MybatisConfig
 *
 * @author Wilson
 * @date 18-7-10
 */
@Configuration
@MapperScan("per.wilson.distributed.dao")
public class MybatisConfig {
    private static final String MAPPER_PATH = "classpath*:mappers/*.xml";
    private static final String MODEL_PACKAGE = "per.wilson.entity.model";

    @Bean
    public TransactionFactory transactionFactory() {
        return new SpringManagedTransactionFactory();
    }

    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        return configuration;
    }

    @Bean("sqlSessionFactory")
    @RefreshScope
    public MybatisSqlSessionFactoryBean sqlSessionFactory(MybatisConfiguration mybatisConfiguration,
                                                          DataSource dataSource,
                                                          GlobalConfiguration globalConfig) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources(MAPPER_PATH));
        // 设置映射类所在包
        sqlSessionFactory.setTypeAliasesPackage(MODEL_PACKAGE);
        sqlSessionFactory.setConfiguration(mybatisConfiguration);
        sqlSessionFactory.setPlugins(new Interceptor[]{
                new PaginationInterceptor(),
                new PerformanceInterceptor(),
                new OptimisticLockerInterceptor(),
                createPageInterceptor()
        });
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("per.wilson.distributed");
        return mapperScannerConfigurer;
    }

    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("1");
        conf.setLogicNotDeleteValue("0");
        conf.setDbType(DbType.MYSQL.getValue());
        conf.setIdType(IdType.ID_WORKER.getKey());
        return conf;
    }

    private PageInterceptor createPageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.put("helperDialect", "postgresql");
        properties.put("offsetAsPageNum", "true");
        properties.put("reasonable", "true");
        properties.put("rowBoundsWithCount", "true");
        properties.put("pageSizeZero", "true");
        properties.put("params", "pageNum=start;pageSize=limit;pageSizeZero=zero;count=countSql");
        properties.put("supportMethodsArguments", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

}
