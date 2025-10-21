package ru.practicum.shareit.booking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item availableItem;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@test.com");
        booker = userRepository.save(booker);

        availableItem = new Item();
        availableItem.setName("Available Item");
        availableItem.setDescription("Test available item");
        availableItem.setAvailable(true);
        availableItem.setOwner(owner.getId());
        availableItem = itemRepository.save(availableItem);
    }

    @Test
    void create() {
        CreateBookingDto bookingRequest = CreateBookingDto.builder()
                .itemId(availableItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto result = bookingService.create(bookingRequest, booker.getId());

        assertNotNull(result);
        assertEquals(availableItem.getId(), result.getItem().getId());
        assertEquals(booker.getId(), result.getBooker().getId());
        assertNotNull(result.getStatus());
    }

    @Test
    void createBooking_UnavailableItem() {
        Item unavailableItem = new Item();
        unavailableItem.setName("Unavailable Item");
        unavailableItem.setDescription("Test unavailable item");
        unavailableItem.setAvailable(false);
        unavailableItem.setOwner(owner.getId());
        unavailableItem = itemRepository.save(unavailableItem);

        CreateBookingDto bookingRequest = CreateBookingDto.builder()
                .itemId(unavailableItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(ValidationException.class, () ->
                bookingService.create(bookingRequest, booker.getId()));
    }

    @Test
    void update() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(availableItem);
        booking.setBooker(booker);
        Booking savedBooking = bookingRepository.save(booking);
        BookingDto result = bookingService.update(owner.getId(), savedBooking.getId(), true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }
}