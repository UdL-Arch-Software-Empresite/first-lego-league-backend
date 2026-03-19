package cat.udl.eps.softarch.fll.controller;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import cat.udl.eps.softarch.fll.service.EditionTeamRegistrationService;
import cat.udl.eps.softarch.fll.domain.Team;
import cat.udl.eps.softarch.fll.controller.dto.TeamResponse;

@RestController
public class EditionTeamController {

	private final EditionTeamRegistrationService registrationService;

	public EditionTeamController(EditionTeamRegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/editions/{editionId}/teams/{teamId}")
	public ResponseEntity<Map<String, String>> registerTeam(
			@PathVariable Long editionId, @PathVariable String teamId) {
		registrationService.registerTeam(editionId, teamId);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
				"editionId", editionId.toString(),
				"teamId", teamId,
				"status", "REGISTERED"));
	}

	@GetMapping("/editions/{editionId}/teams")
	public ResponseEntity<List<TeamResponse>> getTeamsByEdition(
			@PathVariable Long editionId) {
		
		List<Team> teams = registrationService.getTeamsByEdition(editionId);
		
		List<TeamResponse> response = teams.stream()
			.map(team -> new TeamResponse(team.getId(), team.getName()))
			.collect(Collectors.toList());
		
		return ResponseEntity.ok(response);
	}
}

