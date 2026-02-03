package kg.azar.votingbackend.repository;

import kg.azar.votingbackend.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
