package cat.udl.eps.softarch.demo.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.demo.domain.Volunteer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Volunteers", description = "Repository for managing Volunteer entities")
@RepositoryRestResource
public interface VolunteerRepository extends CrudRepository<Volunteer, Long>, PagingAndSortingRepository<Volunteer, Long> {

    @Operation(summary = "Find volunteers by name",
            description = "Returns a list of Volunteers whose name matches the given value.")
    List<Volunteer> findByName(@Param("name") String name);

    @Operation(summary = "Find volunteers by email",
            description = "Returns a list of Volunteers whose email address matches the given value.")
    List<Volunteer> findByEmailAddress(@Param("email") String email);
}

