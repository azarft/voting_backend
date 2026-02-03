package kg.azar.votingbackend.service;

import kg.azar.votingbackend.dto.SessionRequest;
import kg.azar.votingbackend.entity.*;
import kg.azar.votingbackend.repository.OptionRepository;
import kg.azar.votingbackend.repository.UserRepository;
import kg.azar.votingbackend.repository.VoteRepository;
import kg.azar.votingbackend.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final VotingSessionRepository sessionRepository;
    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

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
        // Deactivate any currently active session
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

    public VotingSession getActiveSession() {
        return sessionRepository.findByStatus(VotingSessionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active session found"));
    }

    @Transactional
    public Vote submitVote(String userEmail, Long sessionId, Long optionId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        VotingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != VotingSessionStatus.ACTIVE) {
            throw new RuntimeException("Session is not active");
        }

        if (voteRepository.existsByUserIdAndSessionId(user.getId(), sessionId)) {
            throw new RuntimeException("User already voted in this session");
        }

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        if (!option.getSession().getId().equals(sessionId)) {
            throw new RuntimeException("Option does not belong to this session");
        }

        Vote vote = Vote.builder()
                .user(user)
                .session(session)
                .option(option)
                .timestamp(LocalDateTime.now())
                .build();

        return voteRepository.save(vote);
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

        voteRepository.deleteBySessionId(id);
        sessionRepository.delete(session);
    }
}
