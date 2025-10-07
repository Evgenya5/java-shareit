package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(message = "text не может быть пустым")
    private String text;
    private Long itemId;
    private String authorName;
    private Long authorId;
    private String created;
}
