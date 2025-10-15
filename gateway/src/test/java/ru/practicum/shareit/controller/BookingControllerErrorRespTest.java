package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerErrorRespTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBookingsByBooker() {
        BookingRequestDto bookingRequestDto = mock(BookingRequestDto.class);
        BookingRequestDto bookingRequestDto1 = mock(BookingRequestDto.class);
        Collection<BookingRequestDto> bookings = Arrays.asList(bookingRequestDto, bookingRequestDto1);
        ResponseEntity<Object> response = new ResponseEntity<>(bookings, HttpStatusCode.valueOf(500));
        when(bookingClient.getBookingsByBooker(1L, BookingState.ALL)).thenReturn(response);
        ResponseEntity<Object> result = bookingController.getBookingsByBooker(1L, "ALL");
        assertEquals(response, result);
        verify(bookingClient, times(1)).getBookingsByBooker(1L, BookingState.ALL);
    }

    @Test
    void getBookingsByOwner() {
        BookingRequestDto bookingRequestDto = mock(BookingRequestDto.class);
        BookingRequestDto bookingRequestDto1 = mock(BookingRequestDto.class);
        Collection<BookingRequestDto> bookings = Arrays.asList(bookingRequestDto, bookingRequestDto1);
        ResponseEntity<Object> response = new ResponseEntity<>(bookings, HttpStatusCode.valueOf(500));
        when(bookingClient.getBookingsByOwner(1L, BookingState.ALL)).thenReturn(response);
        ResponseEntity<Object> result = bookingController.getBookingsByOwner(1L, "ALL");
        assertEquals(response, result);
        verify(bookingClient, times(1)).getBookingsByOwner(1L, BookingState.ALL);
    }

    @Test
    void createBooking() {
        BookingRequestDto bookingRequestDto = mock(BookingRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(bookingRequestDto, HttpStatusCode.valueOf(500));
        when(bookingClient.createBooking(1L, bookingRequestDto)).thenReturn(response);
        ResponseEntity<Object> result = bookingController.createBooking(1L, bookingRequestDto);
        assertEquals(response, result);
        verify(bookingClient, times(1)).createBooking(1L, bookingRequestDto);
    }

    @Test
    void getBooking() {
        BookingRequestDto bookingRequestDto = mock(BookingRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(bookingRequestDto, HttpStatusCode.valueOf(500));
        when(bookingClient.getBooking(1L, 1L)).thenReturn(response);
        ResponseEntity<Object> result = bookingController.getBooking(1L, 1L);
        assertEquals(response, result);
        verify(bookingClient, times(1)).getBooking(1L, 1L);
    }

    @Test
    void update() {
        BookingRequestDto bookingRequestDto = mock(BookingRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(bookingRequestDto, HttpStatusCode.valueOf(500));
        when(bookingClient.updateBooking(1L, 1L, true)).thenReturn(response);
        ResponseEntity<Object> result = bookingController.update(1L, 1L, true);
        assertEquals(response, result);
        verify(bookingClient, times(1)).updateBooking(1L, 1L, true);
    }
}