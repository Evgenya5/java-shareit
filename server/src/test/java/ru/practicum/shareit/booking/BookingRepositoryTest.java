package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;
    private Item item;

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
    void findByIdExistingBooking() {
        // Given
        Booking booking = createBooking(item, booker,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING);

        Optional<Booking> result = bookingRepository.findById(booking.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(booking.getId());
        assertThat(result.get().getItem()).isNotNull();
        assertThat(result.get().getBooker()).isNotNull();
    }

    @Test
    void findByIdNotExistBooking() {
        Optional<Booking> result = bookingRepository.findById(9999999L);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllByBookerId() {

        createBooking(item, booker,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING);

        Collection<Booking> result = bookingRepository.findByBooker_IdOrderByStartDesc(booker.getId());
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);

        Booking dto = result.iterator().next();
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getItem()).isNotNull();
        assertThat(dto.getBooker()).isNotNull();
    }

    @Test
    void findAllByOwnerId() {

        createBooking(item, booker,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING);

        Collection<Booking> result = bookingRepository.findByOwner_IdOrderByStartDesc(owner.getId());
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
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