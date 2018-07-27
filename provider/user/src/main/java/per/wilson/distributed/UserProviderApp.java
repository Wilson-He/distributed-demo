package per.wilson.distributed;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import per.wilson.distributed.config.UserProvideBaseConfig;

/**
 * UserProviderApp
 *
 * @author Wilson
 * @date 18-7-10
 */
@SpringBootApplication(scanBasePackageClasses = UserProvideBaseConfig.class)
@RequestMapping("/")
@EnableDubbo
@EnableDiscoveryClient
@EnableAutoConfiguration
//@EnableDiscoveryClient
public class UserProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(UserProviderApp.class);
    }
}
