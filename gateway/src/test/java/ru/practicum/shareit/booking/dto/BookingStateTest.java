package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {

    @Test
    void fromCorrectValueAll() {
        assertTrue(BookingState.from("ALL").isPresent());
    }

    @Test
    void fromCorrectValueWaiting() {
        assertTrue(BookingState.from("WAITING").isPresent());
    }

    @Test
    void fromCorrectValueRejected() {
        assertTrue(BookingState.from("REJECTED").isPresent());
    }

    @Test
    void fromIncorrectValue() {
        assertTrue(BookingState.from("anyText").isEmpty());
    }
}