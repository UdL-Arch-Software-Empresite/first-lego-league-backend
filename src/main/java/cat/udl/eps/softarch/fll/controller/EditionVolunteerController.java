package cat.udl.eps.softarch.fll.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.udl.eps.softarch.fll.controller.dto.ApiErrorResponse;
import cat.udl.eps.softarch.fll.controller.dto.EditionVolunteersResponse;
import cat.udl.eps.softarch.fll.service.EditionVolunteerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/editions")
@RequiredArgsConstructor
public class EditionVolunteerController {

	private final EditionVolunteerService editionVolunteerService;

	@GetMapping("/{editionId}/volunteers")
	public EditionVolunteersResponse getVolunteersGroupedByType(@PathVariable Long editionId) {
		return editionVolunteerService.getVolunteersGroupedByType(editionId);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(
			NoSuchElementException exception,
			HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiErrorResponse.of(exception.getMessage(), exception.getMessage(), request.getRequestURI()));
	}
}
