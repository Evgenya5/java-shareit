package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустой строкой.")
    private String name;

    @NotBlank(message = "Описание не может быть пустой строкой.")
    private String description;

    @NotNull
    private Boolean available;

    private List<String> comments = new ArrayList<>();
    private Long lastBooking;
    private Long nextBooking;
}