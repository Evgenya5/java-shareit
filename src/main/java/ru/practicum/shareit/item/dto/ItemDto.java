package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    @Min(value = 1, message = "Id должно быть положительным числом.")
    private Long id;

    @NotBlank(message = "Название не может быть пустой строкой.")
    @NotNull
    private String name;

    @NotNull
    @NotBlank(message = "Описание не может быть пустой строкой.")
    private String description;

    @NotNull
    private Boolean available;
}
