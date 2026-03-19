package cat.udl.eps.softarch.fll.domain;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class EditionTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "edition_id")
    private Edition edition;

    @ManyToOne
    @JoinColumn(name = "team_name") 
    private Team team;

    private LocalDate registrationDate = LocalDate.now();
}