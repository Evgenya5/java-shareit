package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;
import java.time.format.DateTimeFormatter;

public class BookingMapper {
    private static final DateTimeFormatter dateTimeFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
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
                bookingDto.getStart(),
                bookingDto.getEnd(),
                ItemMapper.toItem(bookingDto.getItem(),null),
                UserMapper.toUser(bookingDto.getBooker()),
                bookingStatus
        );
    }

    public static Booking toBooking(CreateBookingDto createBookingDto) {
        BookingStatus bookingStatus = BookingStatus.WAITING;

        return new Booking(
                createBookingDto.getId(),
                createBookingDto.getStart(),
                createBookingDto.getEnd(),
                null,
                null,
                bookingStatus
        );
    }
}
