package kg.azar.votingbackend.messaging.consumer;

import kg.azar.votingbackend.config.RabbitMQConfig;
import kg.azar.votingbackend.messaging.VoteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VoteConsumer {

    @RabbitListener(queues = RabbitMQConfig.VOTE_QUEUE)
    public void consumeVoteEvent(VoteEvent event) {
        log.info("Consumed VoteEvent: {}", event);
        
        // Simulate async processing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("Vote processed successfully: {}", event.getVoteId());
    }
}
