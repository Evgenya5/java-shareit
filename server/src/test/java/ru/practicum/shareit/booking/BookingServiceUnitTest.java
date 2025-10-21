package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingService bookingService;


    @Test
    void create_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;

        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User booker = new User();
        booker.setId(bookerId);

        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> bookingService.create(requestDto, bookerId));
        verify(bookingRepository, times(0)).save(booking);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(0)).findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void create_ItemNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);

        assertThrows(NotFoundException.class, () -> bookingService.create(requestDto, bookerId));
        verify(bookingRepository, times(0)).save(booking);
        verify(userRepository, times(0)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(0)).findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void create_ItemNotAvailable() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(false);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        assertThrows(ValidationException.class, () -> bookingService.create(requestDto, bookerId));
        verify(bookingRepository, times(0)).save(booking);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(0)).findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void create_BookingAlreadyExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));
        assertThrows(ValidationException.class, () -> bookingService.create(requestDto, bookerId));
        verify(bookingRepository, times(0)).save(booking);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any());

    }

    @Test
    void create() {

        Long bookerId = 2L;
        Long itemId = 1L;

        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User booker = new User();
        booker.setId(bookerId);

        User owner = new User();
        owner.setId(3L);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingRepository.findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of());
        BookingDto result = bookingService.create(requestDto, bookerId);
        assertNotNull(result);
        verify(bookingRepository, times(1)).save(booking);
        verify(bookingRepository, times(1)).findByItem_IdAndEndIsAfterAndStartIsBefore(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void update() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(Mockito.any())).thenReturn(booking);
        BookingDto result = bookingService.update(owner.getId(), booking.getId(), true);
        assertNotNull(result);
        verify(bookingRepository, times(1)).save(Mockito.any());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void update_BookingNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.update(owner.getId(), booking.getId(), true));
        verify(bookingRepository, times(0)).save(Mockito.any());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void update_UserNotOwner() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        assertThrows(ValidationException.class, () -> bookingService.update(5L, booking.getId(), true));
        verify(bookingRepository, times(0)).save(Mockito.any());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void update_StatusNotWaiting() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        assertThrows(ValidationException.class, () -> bookingService.update(owner.getId(), booking.getId(), true));
        verify(bookingRepository, times(0)).save(Mockito.any());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void findById() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        BookingDto result = bookingService.findById(owner.getId(), booking.getId());
        assertNotNull(result);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void findById_BookingNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findById(owner.getId(), booking.getId()));
        verify(userRepository, times(0)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void findById_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        assertThrows(NotFoundException.class, () -> bookingService.findById(owner.getId(), booking.getId()));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void findById_UserNotOwner() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User(777777L, "ds", "a@a.ru")));
        assertThrows(ValidationException.class, () -> bookingService.findById(7L, booking.getId()));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void findByOwner_ALL() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByOwner_IdOrderByStartDesc(owner.getId())).thenReturn(List.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Collection<BookingDto> result = bookingService.findAllByOwner(owner.getId(), "ALL");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByOwner_IdOrderByStartDesc(Mockito.anyLong());
    }

    @Test
    void findByOwner_ALL_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(owner.getId(), "ALL"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByOwner_IdOrderByStartDesc(Mockito.anyLong());
    }

    @Test
    void findByOwner_CURRENT() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByOwner_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Collection<BookingDto> result = bookingService.findAllByOwner(owner.getId(), "CURRENT");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByOwner_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void findByOwner_CURRENT_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(owner.getId(), "CURRENT"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByOwner_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(),any(),any());
    }

    @Test
    void findByOwner_PAST() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByOwner_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Collection<BookingDto> result = bookingService.findAllByOwner(owner.getId(), "PAST");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByOwner_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByOwner_PAST_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(owner.getId(), "PAST"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByOwner_IdAndEndIsBeforeOrderByStartDesc(anyLong(),any());
    }

    @Test
    void findByOwner_FUTURE() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByOwner_IdAndStartIsAfterOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Collection<BookingDto> result = bookingService.findAllByOwner(owner.getId(), "FUTURE");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByOwner_IdAndStartIsAfterOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByOwner_FUTURE_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "FUTURE"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByOwner_IdAndStartIsAfterOrderByStartDesc(Mockito.anyLong(),Mockito.any());
    }

    @Test
    void findByOwner_REJECTED() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByOwner_IdAndStatusOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Collection<BookingDto> result = bookingService.findAllByOwner(owner.getId(), "REJECTED");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByOwner_IdAndStatusOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByOwner_REJECTED_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(owner.getId(), "REJECTED"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByOwner_IdAndStatusOrderByStartDesc(Mockito.anyLong(),Mockito.any());
    }

    @Test
    void findByOwner_WAITING() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByOwner_IdAndStatusOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        Collection<BookingDto> result = bookingService.findAllByOwner(owner.getId(), "WAITING");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByOwner_IdAndStatusOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByOwner_WAITING_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByOwner(owner.getId(), "WAITING"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByOwner_IdAndStatusOrderByStartDesc(Mockito.anyLong(),Mockito.any());
    }

    @Test
    void findByBooker_ALL() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByBooker_IdOrderByStartDesc(bookerId)).thenReturn(List.of(booking));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Collection<BookingDto> result = bookingService.findAllByBooker(booker.getId(), "ALL");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByBooker_IdOrderByStartDesc(Mockito.anyLong());
    }

    @Test
    void findByBooker_ALL_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "ALL"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByBooker_IdOrderByStartDesc(Mockito.anyLong());
    }

    @Test
    void findByBooker_CURRENT() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByBooker_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Collection<BookingDto> result = bookingService.findAllByBooker(booker.getId(), "CURRENT");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByBooker_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void findByBooker_CURRENT_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "CURRENT"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByBooker_IdOrderByStartDesc(Mockito.anyLong());
    }

    @Test
    void findByBooker_PAST() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Collection<BookingDto> result = bookingService.findAllByBooker(booker.getId(), "PAST");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByBooker_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByBooker_PAST_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "PAST"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByBooker_IdOrderByStartDesc(Mockito.anyLong());
    }

    @Test
    void findByBooker_FUTURE() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByBooker_IdAndStartIsAfterOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Collection<BookingDto> result = bookingService.findAllByBooker(booker.getId(), "FUTURE");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByBooker_IdAndStartIsAfterOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByBooker_FUTURE_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "FUTURE"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByBooker_IdAndStartIsAfterOrderByStartDesc(Mockito.anyLong(),Mockito.any());
    }

    @Test
    void findByBooker_REJECTED() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Collection<BookingDto> result = bookingService.findAllByBooker(booker.getId(), "REJECTED");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByBooker_REJECTED_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "REJECTED"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByBooker_IdAndStatusOrderByStartDesc(Mockito.anyLong(),Mockito.any());
    }

    @Test
    void findByBooker_WAITING() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Collection<BookingDto> result = bookingService.findAllByBooker(booker.getId(), "WAITING");
        assertEquals(result.size(), 1);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any());
    }

    @Test
    void findByBooker_WAITING_UserNotExist() {

        Long bookerId = 2L;
        Long itemId = 1L;
        CreateBookingDto requestDto = CreateBookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        User booker = new User();
        booker.setId(bookerId);
        User owner = new User();
        owner.setId(3L);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Booking booking = BookingMapper.toBooking(requestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setId(1L);
        assertThrows(NotFoundException.class, () -> bookingService.findAllByBooker(booker.getId(), "WAITING"));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(0)).findByBooker_IdAndStatusOrderByStartDesc(Mockito.anyLong(),Mockito.any());
    }
}