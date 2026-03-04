package cat.udl.eps.softarch.fll.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cat.udl.eps.softarch.fll.domain.Coach;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.repository.CoachRepository;
import cat.udl.eps.softarch.fll.repository.TeamRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

public class AssignCoachStepDefs {

	private StepDefs stepDefs;
	private TeamRepository teamRepository;
	private CoachRepository coachRepository;
	private ObjectMapper mapper;

	public AssignCoachStepDefs(StepDefs stepDefs,
							   TeamRepository teamRepository,
							   CoachRepository coachRepository) {
		this.stepDefs = stepDefs;
		this.teamRepository = teamRepository;
		this.coachRepository = coachRepository;
		this.mapper = new ObjectMapper();
	}

	@Before
	public void setUp() {
		stepDefs.result = null;
	}

	private ResultActions performAssignCoach(String teamName, Integer coachId) throws Exception {
		return stepDefs.mockMvc.perform(
			post("/teams/assign-coach")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"teamId\":\"" + teamName + "\",\"coachId\":" + coachId + "}")
				.characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)
				.with(user("testuser").roles("COACH")) // <--- autenticación simulada
		);
	}

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
		coach.setName("Coach" + id);
		coach.setEmailAddress("coach" + id + "@mail.com");
		coach.setPhoneNumber("123456789");
		coachRepository.save(coach);
	}

	@Given("coach {int} is assigned to team {string}")
	public void coachAssignedToTeam(Integer coachId, String teamName) throws Exception {
		performAssignCoach(teamName, coachId).andExpect(status().isOk());
	}

	@When("I assign coach {int} to team {string}")
	public void assignCoach(Integer coachId, String teamName) throws Exception {
		stepDefs.result = performAssignCoach(teamName, coachId);
	}

	@Then("the assignment is successful")
	public void assignmentSuccessful() throws Exception {
		stepDefs.result.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("ASSIGNED"));
	}

	@Then("the error {word} is returned")
	public void errorReturned(String error) throws Exception {
		stepDefs.result.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value(error));
	}
}