package cat.udl.eps.softarch.fll.steps;

import cat.udl.eps.softarch.fll.domain.Coach;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.dto.AssignCoachResponse;
import cat.udl.eps.softarch.fll.repository.CoachRepository;
import cat.udl.eps.softarch.fll.repository.TeamRepository;
import cat.udl.eps.softarch.fll.service.CoachService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.cucumber.java.Before;



@SpringBootTest
public class AssignCoachSteps {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private CoachService coachService;

	private Exception exception;
	private AssignCoachResponse response;

	@Given("a team {string} exists")
	public void aTeamExists(String teamName) {
		Team team = new Team();
		team.setName(teamName);
		team.setCity("Lleida");
		team.setFoundationYear(2020);
		team.setCategory("FLL");
		team.setEducationalCenter("School");

		teamRepository.save(team);
	}

	@Given("a coach with id {int} exists")
	public void aCoachExists(Integer id) {
		Coach coach = new Coach();
		coach.setId(id);
		coach.setName("Coach" + id);
		coach.setEmailAddress("coach" + id + "@mail.com");
		coach.setPhoneNumber("123456789");

		coachRepository.save(coach);
	}

	@Given("coach {int} is assigned to team {string}")
	public void coachAssignedToTeam(Integer coachId, String teamName) {
		coachService.assignCoach(teamName, coachId);
	}

	@When("I assign coach {int} to team {string}")
	public void assignCoach(Integer coachId, String teamId) {
		try {
			response = coachService.assignCoach(teamId, coachId);
		} catch (Exception e) {
			exception = e;
		}
	}

	@Then("the assignment is successful")
	public void assignmentSuccessful() {
		assertNotNull(response);
		assertEquals("ASSIGNED", response.getStatus());
	}

	@Then("the error {word} is returned")
	public void errorReturned(String error) {
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains(error));
	}
	@Before
	public void setUp() {
		exception = null;
		response = null;
	}
}

