package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRequestDto {
	@NotBlank(message = "Название не может быть пустой строкой.")
	private String name;
	@NotBlank(message = "Email не может быть пустой строкой.")
	@Email
	private String email;
}
