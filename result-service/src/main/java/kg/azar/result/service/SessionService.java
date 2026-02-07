package kg.azar.result.service;

import kg.azar.result.dto.SessionRequest;
import kg.azar.result.entity.Option;
import kg.azar.result.entity.VotingSession;
import kg.azar.result.entity.VotingSessionStatus;
import kg.azar.result.repository.OptionRepository;
import kg.azar.result.repository.VoteRepository;
import kg.azar.result.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final VotingSessionRepository sessionRepository;
    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public VotingSession createSession(SessionRequest request) {
        VotingSession session = VotingSession.builder()
                .title(request.getTitle())
                .status(VotingSessionStatus.DRAFT)
                .build();
        
        final VotingSession savedSession = sessionRepository.save(session);
        
        List<Option> options = request.getOptions().stream()
                .map(text -> Option.builder()
                        .text(text)
                        .session(savedSession)
                        .build())
                .collect(Collectors.toList());
        
        optionRepository.saveAll(options);
        savedSession.setOptions(options);
        return savedSession;
    }

    @Transactional
    public void activateSession(Long sessionId) {
        sessionRepository.findByStatus(VotingSessionStatus.ACTIVE)
                .ifPresent(s -> {
                    s.setStatus(VotingSessionStatus.CLOSED);
                    sessionRepository.save(s);
                });

        VotingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(VotingSessionStatus.ACTIVE);
        sessionRepository.save(session);
    }

    public Optional<VotingSession> getActiveSession() {
        return sessionRepository.findByStatus(VotingSessionStatus.ACTIVE);
    }

    public List<VotingSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public VotingSession getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @Transactional
    public void closeSession(Long id) {
        VotingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(VotingSessionStatus.CLOSED);
        sessionRepository.save(session);
    }

    @Transactional
    public void deleteSession(Long id) {
        VotingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() == VotingSessionStatus.ACTIVE) {
            throw new RuntimeException("Cannot delete an active session. Close it first.");
        }

        sessionRepository.delete(session);
    }
}
