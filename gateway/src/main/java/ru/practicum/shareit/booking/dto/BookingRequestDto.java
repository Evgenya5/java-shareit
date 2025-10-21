package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookingRequestDto {
	@NotNull(message = "itemId не может быть пустым")
	private Long itemId;
	@FutureOrPresent(message = "start не может быть в прошлом")
	@NotNull(message = "start date не может быть пустым")
	private LocalDateTime start;
	@Future(message = "end date не может быть в прошлом")
	@NotNull(message = "end date не может быть пустым")
	private LocalDateTime end;
}
