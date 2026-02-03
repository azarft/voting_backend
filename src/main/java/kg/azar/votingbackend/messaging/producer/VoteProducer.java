package kg.azar.votingbackend.messaging.producer;

import kg.azar.votingbackend.config.RabbitMQConfig;
import kg.azar.votingbackend.messaging.VoteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendVoteEvent(VoteEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.VOTE_EXCHANGE,
                RabbitMQConfig.VOTE_ROUTING_KEY,
                event
        );
    }
}
