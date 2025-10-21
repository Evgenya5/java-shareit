package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllByBooker() {
        BookingDto request1 = mock(BookingDto.class);
        BookingDto request2 = mock(BookingDto.class);
        Collection<BookingDto> requests = Arrays.asList(request1, request2);
        when(bookingService.findAllByBooker(1L, "ALL")).thenReturn(requests);
        Collection<BookingDto> result = bookingController.findAllByBooker(1L, "ALL");
        assertEquals(2, result.size());
        verify(bookingService, times(1)).findAllByBooker(1L, "ALL");

    }

    @Test
    void findAllByOwner() {
        BookingDto request1 = mock(BookingDto.class);
        BookingDto request2 = mock(BookingDto.class);
        Collection<BookingDto> requests = Arrays.asList(request1, request2);
        when(bookingService.findAllByOwner(1L, "ALL")).thenReturn(requests);
        Collection<BookingDto> result = bookingController.findAllByOwner(1L, "ALL");
        assertEquals(2, result.size());
        verify(bookingService, times(1)).findAllByOwner(1L, "ALL");

    }

    @Test
    void findById() {
        BookingDto request = mock(BookingDto.class);
        when(bookingService.findById(1L, 1L)).thenReturn(request);
        BookingDto result = bookingController.findById(1L, 1L);
        assertEquals(result, request);
        verify(bookingService, times(1)).findById(1L, 1L);
    }

    @Test
    void create() {
        CreateBookingDto createRequest = mock(CreateBookingDto.class);
        BookingDto request = mock(BookingDto.class);
        when(bookingService.create(createRequest, 1L)).thenReturn(request);
        BookingDto result = bookingController.create(1L, createRequest);
        assertEquals(result, request);
        verify(bookingService, times(1)).create(createRequest, 1L);
    }

    @Test
    void update() {
        BookingDto request = mock(BookingDto.class);
        when(bookingService.update(1L, 1L, true)).thenReturn(request);
        BookingDto result = bookingController.update(1L, 1L, true);
        assertEquals(result, request);
        verify(bookingService, times(1)).update(1L, 1L, true);
    }
}