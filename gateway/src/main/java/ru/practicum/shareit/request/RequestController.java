package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
	private final RequestClient requestClient;

	@PostMapping
	public ResponseEntity<Object> requestCreate(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid RequestDto requestDto) {
		//log.info("Creating item {} for user {}", requestDto, userId);
		return requestClient.requestCreate(userId, requestDto);
	}


	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
		//log.info("Get request by Id={}", id);
		return requestClient.getRequest(userId, id);
	}


	@GetMapping
	public ResponseEntity<Object> findAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
		//log.info("Get items by userId={}", userId);
		return requestClient.getRequestsByUser(userId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
		//log.info("Get all items, userId={}", userId);
		return requestClient.getAllRequests(userId);
	}
}
