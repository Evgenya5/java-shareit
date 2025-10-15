package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingState;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {

    @Test
    void fromCorrectValueAll() {
        Assertions.assertTrue(BookingState.from("ALL").isPresent());
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