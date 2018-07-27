package per.wilson.distributed;

import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * UserProviderApp
 *
 * @author Wilson
 * @date 18-7-10
 */
@SpringBootApplication
@Api
public class UserConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(UserConsumerApp.class);
    }
}
