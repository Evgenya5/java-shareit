package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
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
public class UserDto {
    @Min(value = 1, message = "Id должно быть положительным числом.")
    private Long id;

    @NotBlank(message = "Название не может быть пустой строкой.")
    @NotNull
    private String name;

    @NotBlank(message = "Email не может быть пустой строкой.")
    @NotNull
    @Email
    private String email;
}
