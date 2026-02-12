package kg.azar.result.repository;

import kg.azar.result.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    long countByOptionId(Long optionId);
    boolean existsByDeviceIdAndSessionId(String deviceId, Long sessionId);
}
