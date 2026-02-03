package kg.azar.votingbackend.service;

import kg.azar.votingbackend.entity.Option;
import kg.azar.votingbackend.entity.VotingSession;
import kg.azar.votingbackend.repository.VoteRepository;
import kg.azar.votingbackend.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final VoteRepository voteRepository;
    private final VotingSessionRepository sessionRepository;

    @Cacheable(value = "liveResults", key = "'active'")
    public Map<String, Long> getLiveResults() {
        return calculateResults();
    }

    @CachePut(value = "liveResults", key = "'active'")
    public Map<String, Long> refreshLiveResults() {
        return calculateResults();
    }

    private Map<String, Long> calculateResults() {
        VotingSession session = sessionRepository.findByStatus(kg.azar.votingbackend.entity.VotingSessionStatus.ACTIVE)
                .orElse(null);
        
        if (session == null) {
            return new HashMap<>();
        }

        Map<String, Long> results = new HashMap<>();
        for (Option option : session.getOptions()) {
            results.put(option.getText(), voteRepository.countByOptionId(option.getId()));
        }
        return results;
    }
    
    public Map<String, Long> getFinalResults(Long sessionId) {
        VotingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        Map<String, Long> results = new HashMap<>();
        for (Option option : session.getOptions()) {
            results.put(option.getText(), voteRepository.countByOptionId(option.getId()));
        }
        return results;
    }
}
