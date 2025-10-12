package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemRequestDto {
	@NotBlank(message = "Название не может быть пустой строкой.")
	private String name;

	@NotBlank(message = "Описание не может быть пустой строкой.")
	private String description;

	@NotNull
	private Boolean available;
	private Long requestId;
}
