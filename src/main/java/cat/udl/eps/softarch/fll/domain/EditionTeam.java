package cat.udl.eps.softarch.fll.domain;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class EditionTeam {
    @Id
    private Long editionId;
    
    @Id
    private Long teamId;
    
    private LocalDate registrationDate = LocalDate.now();
    
    @ManyToOne
    @JoinColumn(name = "edition_id", insertable = false, updatable = false)
    private Edition edition;
    
    @ManyToOne
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;
}