package kg.azar.votingbackend.repository;

import kg.azar.votingbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndSessionId(Long userId, Long sessionId);
    List<Vote> findBySessionId(Long sessionId);
    long countByOptionId(Long optionId);
    void deleteBySessionId(Long sessionId);
}
