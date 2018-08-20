package per.wilson.distributed.mq.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * MqSender
 *
 * @author Wilson
 * @date 18-8-17
 */
@Component
public class MqSender {
    @Resource
    private AmqpTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        System.err.println("rabbitTemplate:" + rabbitTemplate);
    }
}
