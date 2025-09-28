package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Long id;
    @NotBlank(message = "text не может быть пустым")
    String text;
    Long itemId;
    String authorName;
    Long authorId;
    String created;
}
