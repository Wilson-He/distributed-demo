package per.wilson.distributed.mq.queues;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MqTopicConfig
 *
 * @author Wilson
 * @date 18-8-17
 */
@Configuration
public class MqTopicConfig {

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Queue queueMessage() {
        return new Queue("topic.message");
    }

    @Bean
    public Queue queueMessages() {
        return new Queue("topic.messages");
    }

    @Bean
    public Binding topicBinding(Queue queueMessage,TopicExchange topicExchange){
        return BindingBuilder.bind(queueMessage)
                .to(topicExchange)
                .with("topic.message");
    }

}
