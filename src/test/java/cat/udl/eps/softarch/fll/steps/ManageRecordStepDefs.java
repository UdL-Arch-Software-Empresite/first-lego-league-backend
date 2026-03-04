package cat.udl.eps.softarch.fll.steps;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import cat.udl.eps.softarch.fll.domain.Record;
import cat.udl.eps.softarch.fll.domain.User;
import cat.udl.eps.softarch.fll.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class ManageRecordStepDefs {
	private final StepDefs stepDefs;
	private final UserRepository userRepository;

	public ManageRecordStepDefs(StepDefs stepDefs, UserRepository userRepository) {
		this.stepDefs = stepDefs;
		this.userRepository = userRepository;
	}

	@When("^I create a new record with name \"([^\"]*)\"$")
	public void iCreateANewRecordWithName(String name) throws Throwable {
		cat.udl.eps.softarch.fll.domain.Record record = new cat.udl.eps.softarch.fll.domain.Record();
		record.setName(name);

		stepDefs.result = stepDefs.mockMvc.perform(
				post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(stepDefs.mapper.writeValueAsString(record))
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print());
	}

	@When("^I create a new record with name \"([^\"]*)\" owned by \"([^\"]*)\"$")
	public void iCreateANewRecordWithNameOwnedBy(String name, String ownerName) throws Throwable {
		cat.udl.eps.softarch.fll.domain.Record record = new Record();
		record.setName(name);
		User owner = userRepository.findById(ownerName).orElseThrow();
		record.setOwner(owner);

		stepDefs.result = stepDefs.mockMvc.perform(
				post("/records")
						.contentType(MediaType.APPLICATION_JSON)
						.content(stepDefs.mapper.writeValueAsString(record))
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print());
	}

	@And("^The new record is owned by \"([^\"]*)\"$")
	public void theNewRecordIsOwnedBy(String username) throws Throwable {
		String newRecordUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
		stepDefs.result = stepDefs.mockMvc.perform(
				get(newRecordUri + "/owner")
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print())
				.andExpect(jsonPath("$.username", is(username)));
	}

	@And("^The list of records owned by \"([^\"]*)\" includes one named \"([^\"]*)\"$")
	public void itHasBeenCreatedAUserWithUsername(String username, String resourceName) throws Throwable {
		User owner = userRepository.findById(username).orElseThrow();
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/records/search/findByOwner?user={userUri}", owner.getUri())
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print())
				.andExpect(jsonPath("$._embedded.records[*].name", hasItem(is(resourceName))));
	}
}
