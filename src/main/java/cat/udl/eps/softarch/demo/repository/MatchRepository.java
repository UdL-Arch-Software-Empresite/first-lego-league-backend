package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
