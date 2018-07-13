package per.wilson.distributed;

import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserProviderApp
 *
 * @author Wilson
 * @date 18-7-10
 */
@SpringBootApplication
@RestController
@Api
@RequestMapping("/")
//@ImportResource("classpath*:spring-config.xml")
public class UserConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(UserConsumerApp.class);
    }
}
