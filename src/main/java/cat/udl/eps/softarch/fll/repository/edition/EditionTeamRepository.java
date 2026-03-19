package cat.udl.eps.softarch.fll.repository.edition;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.udl.eps.softarch.fll.domain.EditionTeam;

@Repository
public interface EditionTeamRepository extends JpaRepository<EditionTeam, Long> {
    boolean existsByEditionIdAndTeamId(Long editionId, String teamId);
    List<EditionTeam> findByEditionId(Long editionId);
}