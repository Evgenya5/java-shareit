package validateDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BookingRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testHasAllFields() {
        BookingRequestDto booking = mock(BookingRequestDto.class);
        ReflectionTestUtils.setField(booking, "end", LocalDateTime.now().plusMinutes(2));
        ReflectionTestUtils.setField(booking, "start", LocalDateTime.now().plusMinutes(1));
        ReflectionTestUtils.setField(booking, "itemId", 1L);
        var violations = validator.validate(booking);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testItemIdNull() {
        BookingRequestDto booking = mock(BookingRequestDto.class);
        ReflectionTestUtils.setField(booking, "end", LocalDateTime.now().plusMinutes(2));
        ReflectionTestUtils.setField(booking, "start", LocalDateTime.now().plusMinutes(1));
        ReflectionTestUtils.setField(booking, "itemId", null);
        var violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testStartNull() {
        BookingRequestDto booking = mock(BookingRequestDto.class);
        ReflectionTestUtils.setField(booking, "end", LocalDateTime.now().plusMinutes(2));
        ReflectionTestUtils.setField(booking, "start", null);
        ReflectionTestUtils.setField(booking, "itemId", 1L);
        var violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testEndNull() {
        BookingRequestDto booking = mock(BookingRequestDto.class);
        ReflectionTestUtils.setField(booking, "end", null);
        ReflectionTestUtils.setField(booking, "start", LocalDateTime.now().plusMinutes(1));
        ReflectionTestUtils.setField(booking, "itemId", 1L);
        var violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testStartPast() {
        BookingRequestDto booking = mock(BookingRequestDto.class);
        ReflectionTestUtils.setField(booking, "end", LocalDateTime.now().plusMinutes(2));
        ReflectionTestUtils.setField(booking, "start", LocalDateTime.now().plusMinutes(-1));
        ReflectionTestUtils.setField(booking, "itemId", 1L);
        var violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testEndPast() {
        BookingRequestDto booking = mock(BookingRequestDto.class);
        ReflectionTestUtils.setField(booking, "end", LocalDateTime.now().plusMinutes(-2));
        ReflectionTestUtils.setField(booking, "start", LocalDateTime.now().plusMinutes(1));
        ReflectionTestUtils.setField(booking, "itemId", 1L);
        var violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
    }

}