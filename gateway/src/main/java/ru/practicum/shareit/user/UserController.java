package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
	private final UserClient userClient;

	@GetMapping
	public ResponseEntity<Object> getUsers() {
		//log.info("Get users");
		return userClient.getUsers();
	}

	@PostMapping
	public ResponseEntity<Object> userCreate(@RequestBody @Valid UserRequestDto requestDto) {
		//log.info("Creating user {}", requestDto);
		return userClient.userCreate(requestDto);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Object> getUser(@PathVariable Long userId) {
		//log.info("Get userId={}", userId);
		return userClient.getUser(userId);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<Object> update(@PathVariable Long userId, @RequestBody UserRequestDto requestDto) {
		//log.info("Update user {}, userId = {}", requestDto, userId);
		return userClient.userUpdate(userId, requestDto);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Object> delete(@PathVariable Long userId) {
		//log.info("Delete userId={}", userId);
		return userClient.userDelete(userId);
	}
}
