package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "floaters")
@Data
@EqualsAndHashCode(callSuper = true)
public class Floater extends Volunteer {

    @NotBlank
    private String studentCode;
}


