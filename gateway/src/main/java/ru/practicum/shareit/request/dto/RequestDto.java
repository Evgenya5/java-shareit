package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestDto {
	@NotBlank(message = "description не может быть пустым")
	private String description;
}
