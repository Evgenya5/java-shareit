package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
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
        if (!(Objects.equals(userId, booking.getBooker().getId()) || Objects.equals(userId, booking.getItem().getOwner()))) {
            throw new ValidationException("Информацию по бронированию может получить только владелец вещи или создатель бронирования");
        }
        return BookingMapper.toBookingDto(booking);
    }

    public Collection<BookingDto> findAllByBooker(Long userId, String stateStr) {
        State state = State.valueOf(stateStr);
        switch (state) {
            case ALL -> {
                return bookingRepository.findByBooker_IdOrderByStartDesc(userId).stream().map(BookingMapper::toBookingDto).toList();
            }
            default -> {
                return List.of();
            }
        }
    }

    public Collection<BookingDto> findAllByOwner(Long userId, String stateStr) {
        State state = State.valueOf(stateStr);
        switch (state) {
            case ALL -> {
                return bookingRepository.findByOwner_IdOrderByStartDesc(userId).stream().map(BookingMapper::toBookingDto).toList();
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
        if (approved) {
            oldBooking.setStatus(Status.APPROVED);
            item.setAvailable(false);
            itemRepository.save(item);
        } else {
            oldBooking.setStatus(Status.REJECTED);
        }
        // если найдена и все условия соблюдены, обновляем её содержимое
        log.debug("update booking with id: {}", oldBooking.getId());
        bookingRepository.save(oldBooking);
        return BookingMapper.toBookingDto(oldBooking);
    }
}