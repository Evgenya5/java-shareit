package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public Collection<BookingDto> findAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findAllByOwner(userId, state);
    }

    @GetMapping("/{id}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long id) {
        return bookingService.findById(userId, id);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody CreateBookingDto createBookingDto) {
        return bookingService.create(createBookingDto, userId);
    }

    @PatchMapping("/{id}")
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestParam Boolean approved) {
        return bookingService.update(userId, id, approved);
    }
}