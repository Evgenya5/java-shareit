package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookingRequestDto {
	@NotNull(message = "itemId не может быть пустым")
	private long itemId;
	@FutureOrPresent(message = "start не может быть в прошлом")
	private LocalDateTime start;
	@Future(message = "end не может быть в прошлом")
	private LocalDateTime end;
}
