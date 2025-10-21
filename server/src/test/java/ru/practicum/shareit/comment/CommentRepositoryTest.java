package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные с уникальными идентификаторами
        owner = createUser("owner@test.com", "Test Owner");
        booker = createUser("booker@test.com", "Repo Test Booker");
        item = createItem("Item", "Test description", owner, true);

        // Очищаем кэш после создания данных
        entityManager.clear();
    }

    @Test
    void createComment() {

        Comment comment = new Comment();
        comment.setText("text");
        comment.setItemId(item.getId());
        comment.setAuthor(booker);
        Comment result = commentRepository.save(comment);
        assertThat(result.getText()).isEqualTo(comment.getText());
        assertThat(result.getId()).isNotNull();
        assertThat(result.getItemId()).isNotNull();
        assertThat(result.getAuthor()).isNotNull();
    }

    private User createUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    private Item createItem(String name, String description, User owner, boolean available) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner.getId());
        entityManager.persist(item);
        entityManager.flush();
        return item;
    }

    private Booking createBooking(Item item, User booker, LocalDateTime start, LocalDateTime end, BookingStatus status) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        entityManager.persist(booking);
        entityManager.flush();
        return booking;
    }
}