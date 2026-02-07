package kg.azar.result.service;

import kg.azar.result.entity.Option;
import kg.azar.result.entity.VotingSession;
import kg.azar.result.entity.VotingSessionStatus;
import kg.azar.result.repository.VoteRepository;
import kg.azar.result.repository.VotingSessionRepository;
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
        VotingSession session = sessionRepository.findByStatus(VotingSessionStatus.ACTIVE)
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
        return getResultsForSession(session);
    }

    public Map<String, Long> getLatestFinalResults() {
        // Find latest closed session
        return sessionRepository.findAll().stream()
                .filter(s -> s.getStatus() == VotingSessionStatus.CLOSED)
                .sorted((s1, s2) -> s2.getId().compareTo(s1.getId()))
                .findFirst()
                .map(this::getResultsForSession)
                .orElse(new HashMap<>());
    }

    private Map<String, Long> getResultsForSession(VotingSession session) {
        Map<String, Long> results = new HashMap<>();
        for (Option option : session.getOptions()) {
            results.put(option.getText(), voteRepository.countByOptionId(option.getId()));
        }
        return results;
    }
}
