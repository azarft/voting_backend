package kg.azar.result.messaging;

import kg.azar.common.messaging.VoteEvent;
import kg.azar.result.entity.Option;
import kg.azar.result.entity.Vote;
import kg.azar.result.entity.VotingSession;
import kg.azar.result.entity.VotingSessionStatus;
import kg.azar.result.repository.OptionRepository;
import kg.azar.result.repository.VoteRepository;
import kg.azar.result.repository.VotingSessionRepository;
import kg.azar.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteConsumer {

    private final VoteRepository voteRepository;
    private final VotingSessionRepository sessionRepository;
    private final OptionRepository optionRepository;
    private final ResultService resultService;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void consumeVote(VoteEvent event) {
        log.info("Received vote event: {}", event);

        if (voteRepository.existsByUserIdAndSessionId(event.getUserId(), event.getSessionId())) {
            log.warn("Duplicate vote detected for user {} in session {}", event.getUserId(), event.getSessionId());
            return;
        }

        VotingSession session = sessionRepository.findById(event.getSessionId())
                .orElse(null);
        if (session == null || session.getStatus() != VotingSessionStatus.ACTIVE) {
            log.warn("Vote for inactive or non-existent session: {}", event.getSessionId());
            return;
        }

        Option option = optionRepository.findById(event.getOptionId()).orElse(null);
        if (option == null || !option.getSession().getId().equals(session.getId())) {
            log.warn("Invalid option {} for session {}", event.getOptionId(), event.getSessionId());
            return;
        }

        Vote vote = Vote.builder()
                .userId(event.getUserId())
                .session(session)
                .option(option)
                .timestamp(event.getTimestamp())
                .build();

        voteRepository.save(vote);
        log.info("Vote persisted for user {} in session {}", event.getUserId(), event.getSessionId());
    }
}
