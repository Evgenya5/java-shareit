package ru.practicum.shareit.comment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "text")
    String text;
    @Column(name = "item_id")
    Long itemId;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    @Column(name = "created")
    LocalDate created;
}