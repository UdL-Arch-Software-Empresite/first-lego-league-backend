package cat.udl.eps.softarch.fll.controller;

import cat.udl.eps.softarch.fll.dto.AssignCoachRequest;
import cat.udl.eps.softarch.fll.dto.AssignCoachResponse;
import cat.udl.eps.softarch.fll.controller.dto.ApiErrorResponse;
import cat.udl.eps.softarch.fll.service.CoachService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamCoachController {

	private final CoachService teamCoachService;

	@PostMapping("/assign-coach")
	public AssignCoachResponse assignCoach(@Valid @RequestBody AssignCoachRequest request) {
		return teamCoachService.assignCoach(
			request.getTeamId(),
			request.getCoachId()
		);
	}

	@ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
	public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
		HttpStatus status = ex instanceof NoSuchElementException ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status)
			.body(ApiErrorResponse.of(ex.getMessage(), ex.getMessage(), request.getRequestURI()));
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiErrorResponse> handleConflict(IllegalStateException ex, HttpServletRequest request) {
		String msg = ex.getMessage();
		if ("COACH_ALREADY_ASSIGNED".equals(msg)
			|| "MAX_COACHES_PER_TEAM_REACHED".equals(msg)
			|| "MAX_TEAMS_PER_COACH_REACHED".equals(msg)) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiErrorResponse.of(msg, msg, request.getRequestURI()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ApiErrorResponse.of(msg, msg, request.getRequestURI()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiErrorResponse.of("INTERNAL_SERVER_ERROR", "Internal server error", request.getRequestURI()));
	}
}
