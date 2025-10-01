package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingMapper {
    private static final DateTimeFormatter dateTimeFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart().format(dateTimeFormater),
                booking.getEnd().format(dateTimeFormater),
                booking.getItem().getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        BookingStatus bookingStatus = BookingStatus.WAITING;

        if (bookingDto.getStatus() != null) {
            bookingStatus = bookingDto.getStatus();
        }
        return new Booking(
                bookingDto.getId(),
                LocalDateTime.parse(bookingDto.getStart(), dateTimeFormater),
                LocalDateTime.parse(bookingDto.getEnd(), dateTimeFormater),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingStatus
        );
    }
}
