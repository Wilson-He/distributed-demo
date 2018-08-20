package per.wilson.distributed.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MqDirectConfig
 *
 * @author Wilson
 * @date 18-8-17
 */
@Configuration
public class MqDirectConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    @Bean
    public Queue userQueue() {
        return new Queue("user");
    }
}
