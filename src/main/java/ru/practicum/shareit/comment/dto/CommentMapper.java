package ru.practicum.shareit.comment.dto;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, User author) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItemId(),
                author,
                LocalDate.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItemId(),
                comment.getAuthor().getName(),
                comment.getAuthor().getId(),
                comment.getCreated().toString()
        );
    }
}