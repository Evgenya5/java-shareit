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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

//import static org.assertj.core.api.Assertions.assertThat;

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
    void createBooking_WithValidData_ShouldCreateBooking() {
        CreateBookingDto bookingRequest = CreateBookingDto.builder()
                .itemId(availableItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto result = bookingService.create(bookingRequest, booker.getId());

        assertNotNull(result);
        assertEquals(availableItem.getId(), result.getItem().getId());
        assertEquals(booker.getId(), result.getBooker().getId());
        // Статус должен быть WAITING
        assertNotNull(result.getStatus());
    }
    /*

    @Test
    void createBooking_WithUnavailableItem_ShouldThrowException() {
        Item unavailableItem = new Item();
        unavailableItem.setName("Unavailable Item");
        unavailableItem.setDescription("Test unavailable item");
        unavailableItem.setAvailable(false);
        unavailableItem.setOwner(owner);
        unavailableItem = itemRepository.save(unavailableItem);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(unavailableItem.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(NotFoundException.class, () ->
                bookingService.createBooking(booker.getId(), bookingRequest));
    }

    @Test
    void createBooking_ByOwner_ShouldThrowException() {

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(availableItem.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(ValidationException.class, () ->
                bookingService.createBooking(owner.getId(), bookingRequest));
    }

    @Test
    void updateAvailableStatusBooking_ApproveBooking_ShouldUpdateStatus() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(ru.practicum.enums.BookingStatus.WAITING);
        booking.setItem(availableItem);
        booking.setBooker(booker);
        Booking savedBooking = bookingRepository.save(booking);

        BookingDto result = bookingService.updateAvailableStatusBooking(owner.getId(), savedBooking.getId(), true);

        assertNotNull(result);
        assertEquals(ru.practicum.enums.BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void getAllBookingByBookerId_WithDifferentStates_ShouldReturnCorrectBookings() {
        createTestBookings();

        Collection<BookingDto> allBookings = bookingService.getAllBookingByBookerId(booker.getId(), State.ALL);
        assertThat(allBookings).hasSize(2);

        Collection<BookingDto> futureBookings = bookingService.getAllBookingByBookerId(booker.getId(), State.FUTURE);
        assertThat(futureBookings).hasSize(2);

        Collection<BookingDto> waitingBookings = bookingService.getAllBookingByBookerId(booker.getId(), State.WAITING);
        assertThat(waitingBookings).hasSize(1);
    }

    @Test
    void getBookingById_WithValidUser_ShouldReturnBooking() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(ru.practicum.enums.BookingStatus.WAITING);
        booking.setItem(availableItem);
        booking.setBooker(booker);
        Booking savedBooking = bookingRepository.save(booking);

        BookingDto result = bookingService.getBookingById(booker.getId(), savedBooking.getId());

        assertNotNull(result);
        assertEquals(savedBooking.getId(), result.getId());
    }*/

    private void createTestBookings() {
        Booking futureWaiting = new Booking();
        futureWaiting.setStart(LocalDateTime.now().plusDays(1));
        futureWaiting.setEnd(LocalDateTime.now().plusDays(2));
        futureWaiting.setStatus(BookingStatus.WAITING);
        futureWaiting.setItem(availableItem);
        futureWaiting.setBooker(booker);
        bookingRepository.save(futureWaiting);

        Booking futureApproved = new Booking();
        futureApproved.setStart(LocalDateTime.now().plusDays(3));
        futureApproved.setEnd(LocalDateTime.now().plusDays(4));
        futureApproved.setStatus(BookingStatus.APPROVED);
        futureApproved.setItem(availableItem);
        futureApproved.setBooker(booker);
        bookingRepository.save(futureApproved);
    }
}