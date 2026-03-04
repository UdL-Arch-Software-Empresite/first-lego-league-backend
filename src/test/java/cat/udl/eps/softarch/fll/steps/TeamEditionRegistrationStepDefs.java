package cat.udl.eps.softarch.fll.steps;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.stream.IntStream;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.fll.domain.Edition;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.repository.EditionRepository;
import cat.udl.eps.softarch.fll.repository.TeamRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class TeamEditionRegistrationStepDefs {

	private final StepDefs stepDefs;
	private final ManageEditionStepDefs manageEditionStepDefs;
	private final EditionRepository editionRepository;
	private final TeamRepository teamRepository;

	public TeamEditionRegistrationStepDefs(StepDefs stepDefs,
			ManageEditionStepDefs manageEditionStepDefs,
			EditionRepository editionRepository,
			TeamRepository teamRepository) {
		this.stepDefs = stepDefs;
		this.manageEditionStepDefs = manageEditionStepDefs;
		this.editionRepository = editionRepository;
		this.teamRepository = teamRepository;
	}

	@Given("There is a team named {string} from {string} with category {string}")
	public void thereIsATeam(String name, String city, String category) {
		if (!teamRepository.existsById(name)) {
			Team team = new Team(name);
			team.setCity(city);
			team.setFoundationYear(2000);
			team.setCategory(category);
			teamRepository.save(team);
		}
	}

	@Given("Team {string} is already registered in the current edition")
	@Transactional
	public void teamIsAlreadyRegistered(String teamName) {
		Edition edition = editionRepository.findById(currentEditionId()).orElseThrow();
		Team team = teamRepository.findById(teamName).orElseThrow();
		edition.getTeams().add(team);
		editionRepository.save(edition);
	}

	@Given("The current edition already has {int} teams registered")
	@Transactional
	public void editionHasTeamsRegistered(int count) {
		Edition edition = editionRepository.findById(currentEditionId()).orElseThrow();
		IntStream.range(0, count).forEach(i -> {
			String teamName = "FillerTeam" + i;
			Team team = new Team(teamName);
			team.setCity("Igualada");
			team.setFoundationYear(2000);
			team.setCategory("Challenge");
			teamRepository.save(team);
			edition.getTeams().add(team);
		});
		editionRepository.save(edition);
	}

	@When("I register team {string} to the current edition")
	public void iRegisterTeamToCurrentEdition(String teamName) throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				post("/editions/" + currentEditionId() + "/teams/" + teamName)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print());
	}

	@When("I register team {string} to edition with id {long}")
	public void iRegisterTeamToEditionById(String teamName, Long editionId) throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				post("/editions/" + editionId + "/teams/" + teamName)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print());
	}

	@And("The response has status {string}")
	public void theResponseHasStatus(String status) throws Exception {
		stepDefs.result.andExpect(jsonPath("$.status", is(status)));
	}

	@And("The response has error {string}")
	public void theResponseHasError(String error) throws Exception {
		stepDefs.result.andExpect(jsonPath("$.error", is(error)));
	}

	private Long currentEditionId() {
		String uri = manageEditionStepDefs.editionUri;
		return Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
	}
}



