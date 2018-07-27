package per.wilson.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RootApplication
 *
 * @author Wilson
 * @date 18-7-23
 */
@SpringBootApplication
@RestController
@RequestMapping("/")
public class BusinessConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessConsumerApplication.class);
    }
}
