package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустой строкой.")
    private String name;

    @NotBlank(message = "Email не может быть пустой строкой.")
    @Email
    private String email;
}
