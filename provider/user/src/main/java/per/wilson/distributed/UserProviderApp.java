package per.wilson.distributed;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * UserProviderApp
 *
 * @author Wilson
 * @date 18-7-10
 */
@SpringBootApplication
@RequestMapping("/")
@EnableDubbo
public class UserProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(UserProviderApp.class);
    }
}
