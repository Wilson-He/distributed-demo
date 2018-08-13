package per.wilson.distributed;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.wilson.distributed.config.UserProviderConfig;

/**
 * UserProviderApp
 *
 * @author Wilson
 * @date 18-7-10
 */
@SpringBootApplication(scanBasePackageClasses = UserProviderConfig.class)
@EnableDubbo
public class UserProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(UserProviderApp.class);
    }

}
