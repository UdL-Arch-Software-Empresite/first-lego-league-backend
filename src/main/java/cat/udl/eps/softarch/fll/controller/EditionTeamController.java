package cat.udl.eps.softarch.fll.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.udl.eps.softarch.fll.service.EditionTeamRegistrationException;
import cat.udl.eps.softarch.fll.service.EditionTeamRegistrationService;

@RestController
public class EditionTeamController {

	private final EditionTeamRegistrationService registrationService;

	public EditionTeamController(EditionTeamRegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PostMapping("/editions/{editionId}/teams/{teamId}")
	public ResponseEntity<Map<String, String>> registerTeam(
			@PathVariable Long editionId, @PathVariable String teamId) {
		try {
			registrationService.registerTeam(editionId, teamId);
			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
					"editionId", editionId.toString(),
					"teamId", teamId,
					"status", "REGISTERED"));
		} catch (EditionTeamRegistrationException e) {
			HttpStatus status = resolveStatus(e.getError());
			return ResponseEntity.status(status).body(Map.of(
					"error", e.getError(),
					"message", e.getMessage()));
		}
	}

	private HttpStatus resolveStatus(String error) {
		return switch (error) {
			case "EDITION_NOT_FOUND", "TEAM_NOT_FOUND" -> HttpStatus.NOT_FOUND;
			case "MAX_TEAMS_REACHED" -> HttpStatus.CONFLICT;
			case "TEAM_ALREADY_REGISTERED" -> HttpStatus.CONFLICT;
			default -> HttpStatus.BAD_REQUEST;
		};
	}
}

