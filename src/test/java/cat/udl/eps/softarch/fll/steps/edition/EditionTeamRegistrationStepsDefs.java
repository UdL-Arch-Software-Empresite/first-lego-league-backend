package cat.udl.eps.softarch.fll.steps.edition;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cat.udl.eps.softarch.fll.domain.Edition;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.repository.edition.EditionRepository;
import cat.udl.eps.softarch.fll.repository.team.TeamRepository;
import cat.udl.eps.softarch.fll.steps.app.AuthenticationStepDefs;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

public class EditionTeamRegistrationStepsDefs {

    private final StepDefs stepDefs;
    private final EditionRepository editionRepository;
    private final TeamRepository teamRepository;

    public EditionTeamRegistrationStepsDefs(
            StepDefs stepDefs,
            EditionRepository editionRepository,
            TeamRepository teamRepository) {
        this.stepDefs = stepDefs;
        this.editionRepository = editionRepository;
        this.teamRepository = teamRepository;
    }

    @Given("an edition with id {long} exists")
    public void anEditionWithIdExists(Long editionId) {
        if (!editionRepository.findById(editionId).isPresent()) {
            Edition edition = Edition.create(editionId.intValue(), "Test Venue", "Test Description");
            editionRepository.save(edition);
        }
    }

    @Given("a team with name {string} exists")
    public void aTeamWithNameExists(String teamName) {
        if (!teamRepository.findById(teamName).isPresent()) {
            Team team = Team.create(teamName, "Test City", 2020, "Test Category");
            teamRepository.save(team);
        }
    }

    @Given("team {string} is already registered in edition {long}")
    public void teamIsAlreadyRegisteredInEdition(String teamName, Long editionId) throws Exception {
        registerTeam(teamName, editionId);
    }

    @When("I register team {string} in edition {long}")
    public void iRegisterTeamInEdition(String teamName, Long editionId) throws Exception {
        registerTeam(teamName, editionId);
    }

    private void registerTeam(String teamName, Long editionId) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/editions/" + editionId + "/teams/" + teamName)
                    .with(AuthenticationStepDefs.authenticate())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
    }

    @When("I request the list of teams for edition {long}")
    public void iRequestTheListOfTeamsForEdition(Long editionId) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/editions/" + editionId + "/teams")
                    .with(AuthenticationStepDefs.authenticate())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
    }

    @Then("the response status should be {int} Created")
    public void theResponseStatusShouldBeCreated(int expectedStatus) throws Exception {
        stepDefs.result.andExpect(status().isCreated());
    }

    @Then("the response status should be {int} Conflict")
    public void theResponseStatusShouldBeConflict(int expectedStatus) throws Exception {
        stepDefs.result.andExpect(status().isConflict());
    }

    @Then("the response status should be {int} OK")
    public void theResponseStatusShouldBeOk(int expectedStatus) throws Exception {
        stepDefs.result.andExpect(status().isOk());
    }

    @Then("the response should contain team {string}")
    public void theResponseShouldContainTeam(String teamName) throws Exception {
        stepDefs.result.andExpect(jsonPath("$[?(@.id=='" + teamName + "')]").exists());
    }
} 
