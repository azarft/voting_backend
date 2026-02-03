package kg.azar.votingbackend.repository;

import kg.azar.votingbackend.entity.VotingSession;
import kg.azar.votingbackend.entity.VotingSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
    Optional<VotingSession> findByStatus(VotingSessionStatus status);
}
