package kg.azar.result.repository;

import kg.azar.result.entity.VotingSession;
import kg.azar.result.entity.VotingSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
    Optional<VotingSession> findByStatus(VotingSessionStatus status);
}
