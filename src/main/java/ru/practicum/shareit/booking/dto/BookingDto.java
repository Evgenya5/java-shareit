package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingDto {
    private Long id;
    @NotBlank(message = "start date не может быть пустой")
    private String start;
    @NotBlank(message = "end date не может быть пустой")
    private String end;
    @NotNull(message = "itemId не может быть пустым")
    private Long itemId;
    private Item item;
    private User booker;
    private BookingStatus status;
}
