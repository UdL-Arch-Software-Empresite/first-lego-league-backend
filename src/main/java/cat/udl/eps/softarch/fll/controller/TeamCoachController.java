package cat.udl.eps.softarch.fll.controller;

import cat.udl.eps.softarch.fll.dto.AssignCoachRequest;
import cat.udl.eps.softarch.fll.dto.AssignCoachResponse;
import cat.udl.eps.softarch.fll.service.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamCoachController {

	private final CoachService teamCoachService;

	@PostMapping("/assign-coach")
	public AssignCoachResponse assignCoach(@RequestBody AssignCoachRequest request) {

		return teamCoachService.assignCoach(
			request.getTeamId(),
			request.getCoachId()
		);
	}
	@ExceptionHandler(RuntimeException.class)
		public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
			String message = ex.getMessage();
			HttpStatus status = switch (message) {
				case "TEAM_NOT_FOUND", "COACH_NOT_FOUND" -> HttpStatus.NOT_FOUND;
				case "COACH_ALREADY_ASSIGNED" -> HttpStatus.CONFLICT;
				default -> HttpStatus.BAD_REQUEST;
			};
			return ResponseEntity.status(status).body(Map.of("error", message));
		}
}