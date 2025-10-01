package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public BookingDto findById(Long userId, Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Бронирование с id = " + id + " не найдено"));
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        if (!(Objects.equals(userId, booking.getBooker().getId()) || Objects.equals(userId, booking.getItem().getOwner()))) {
            throw new ValidationException("Информацию по бронированию может получить только владелец вещи или создатель бронирования");
        }
        return BookingMapper.toBookingDto(booking);
    }

    public Collection<BookingDto> findAllByBooker(Long userId, String stateStr) {
        BookingState bookingState = BookingState.valueOf(stateStr);
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        switch (bookingState) {
            case ALL -> {
                return bookingRepository.findByBooker_IdOrderByStartDesc(userId).stream().map(BookingMapper::toBookingDto).toList();
            }
            case CURRENT -> {
                return bookingRepository.findByBooker_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case PAST -> {
                return bookingRepository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case FUTURE -> {
                return bookingRepository.findByBooker_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case REJECTED -> {
                return bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case WAITING -> {
                return bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            default -> {
                return List.of();
            }
        }
    }

    public Collection<BookingDto> findAllByOwner(Long userId, String stateStr) {
        BookingState bookingState = BookingState.valueOf(stateStr);
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        switch (bookingState) {
            case ALL -> {
                return bookingRepository.findByOwner_IdOrderByStartDesc(userId).stream().map(BookingMapper::toBookingDto).toList();
            }
            case CURRENT -> {
                return bookingRepository.findByOwner_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case PAST -> {
                return bookingRepository.findByOwner_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case FUTURE -> {
                return bookingRepository.findByOwner_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case REJECTED -> {
                return bookingRepository.findByOwner_IdAndStatusOrderByStartDesc(userId, "REJECTED")
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case WAITING -> {
                return bookingRepository.findByOwner_IdAndStatusOrderByStartDesc(userId, "WAITING")
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            default -> {
                return List.of();
            }
        }
    }

    public BookingDto create(BookingDto bookingDto, Long userId) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + bookingDto.getItemId() + " не найден"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (!bookingRepository.findByItem_IdAndEndIsAfterAndStartIsBefore(item.getId(), BookingMapper.toBooking(bookingDto).getStart(), BookingMapper.toBooking(bookingDto).getEnd()).isEmpty()) {
            throw new ValidationException("Вещь забронирована на данный период");
        }

        bookingDto.setBooker(user);
        bookingDto.setItem(item);
        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto)));
    }

    public BookingDto update(Long userId, Long id, Boolean approved) {

        Booking oldBooking = bookingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Бронирование с id = " + id + " не найдено"));
        Item item = oldBooking.getItem();
        if (!Objects.equals(item.getOwner(), userId)) {
            throw new ValidationException("Вещь не принадлежит пользователю с ид = " + userId);
        }
        if (!oldBooking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Бронирование уже завершено");
        }
        if (approved) {
            oldBooking.setStatus(BookingStatus.APPROVED);
        } else {
            oldBooking.setStatus(BookingStatus.REJECTED);
        }
        // если найдена и все условия соблюдены, обновляем её содержимое
        log.debug("update booking with id: {}", oldBooking.getId());
        bookingRepository.save(oldBooking);
        return BookingMapper.toBookingDto(oldBooking);
    }
}