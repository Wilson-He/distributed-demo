package per.wilson.distributed.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.nio.charset.Charset;

/**
 * BasicConfig
 *
 * @author Wilson
 * @date 18-7-11
 */
@Configuration
public class BaseConfig {
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }
/*

    @Bean
    public BeanValidationPostProcessor beanValidationPostProcessor(LocalValidatorFactoryBean localValidatorFactoryBean) {
        BeanValidationPostProcessor postProcessor = new BeanValidationPostProcessor();
        postProcessor.setValidator(localValidatorFactoryBean);
        return postProcessor;
    }
*/

    @Bean
    public MessageSourceProperties messageSourceProperties() {
        MessageSourceProperties messageSourceProperties = new MessageSourceProperties();
        messageSourceProperties.setEncoding(Charset.forName("UTF-8"));
        messageSourceProperties.setBasename("i18n.properties");
        messageSourceProperties.setUseCodeAsDefaultMessage(false);
        return messageSourceProperties;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(ReloadableResourceBundleMessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        factoryBean.setProviderClass(HibernateValidator.class);
        return factoryBean;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:i18n");
        source.setUseCodeAsDefaultMessage(false);
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(5);
        return source;
    }
/*
    @Bean
    public MessageCodesResolver messageCodesResolver(EnMessageCodeFormatter formatter) {
        DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
        resolver.setMessageCodeFormatter(formatter);
        return new DefaultMessageCodesResolver();
    }*/

}
