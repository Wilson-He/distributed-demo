package per.wilson.distributed.mq.queues;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MqQueues
 *
 * @author Wilson
 * @date 18-8-17
 */
@Configuration
public class MqFanoutConfig {

    @Bean
    public  FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    public Queue fanoutA() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue fanoutB() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue fanoutC() {
        return new Queue("fanout.C");
    }

    @Bean
    public Binding bindingFanoutMessage(Queue fanoutA,FanoutExchange exchange){
        return BindingBuilder.bind(fanoutA)
                .to(exchange);
    }

}
