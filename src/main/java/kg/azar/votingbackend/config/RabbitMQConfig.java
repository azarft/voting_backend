package kg.azar.votingbackend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String VOTE_QUEUE = "vote_queue";
    public static final String VOTE_EXCHANGE = "vote_exchange";
    public static final String VOTE_ROUTING_KEY = "vote_routing_key";

    @Bean
    public Queue voteQueue() {
        return new Queue(VOTE_QUEUE);
    }

    @Bean
    public TopicExchange voteExchange() {
        return new TopicExchange(VOTE_EXCHANGE);
    }

    @Bean
    public Binding voteBinding(Queue voteQueue, TopicExchange voteExchange) {
        return BindingBuilder.bind(voteQueue).to(voteExchange).with(VOTE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
