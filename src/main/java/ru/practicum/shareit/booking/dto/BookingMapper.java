package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import java.time.Instant;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart().toString().substring(0, booking.getStart().toString().length() - 1),
                booking.getEnd().toString().substring(0, booking.getEnd().toString().length() - 1),
                booking.getItem().getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Status status = Status.WAITING;
        if (bookingDto.getStatus() != null) {
            status = bookingDto.getStatus();
        }
        return new Booking(
                bookingDto.getId(),
                Instant.parse(bookingDto.getStart() + "Z"),
                Instant.parse(bookingDto.getEnd() + "Z"),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                status
        );
    }
}
