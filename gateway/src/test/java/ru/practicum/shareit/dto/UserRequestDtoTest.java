package ru.practicum.shareit.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.user.dto.UserRequestDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UserRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmailNull() {
        UserRequestDto user = mock(UserRequestDto.class);
        ReflectionTestUtils.setField(user, "name", "name");
        ReflectionTestUtils.setField(user, "email", null);
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testEmailBlank() {
        UserRequestDto user = mock(UserRequestDto.class);
        ReflectionTestUtils.setField(user, "name", "name");
        ReflectionTestUtils.setField(user, "email", "");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testEmailNotContainsSpecialSymbol() {
        UserRequestDto user = mock(UserRequestDto.class);
        ReflectionTestUtils.setField(user, "name", "name");
        ReflectionTestUtils.setField(user, "email", "aa.ry");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNameNull() {
        UserRequestDto user = mock(UserRequestDto.class);
        ReflectionTestUtils.setField(user, "name", null);
        ReflectionTestUtils.setField(user, "email", "a@a.ru");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNameBlank() {
        UserRequestDto user = mock(UserRequestDto.class);
        ReflectionTestUtils.setField(user, "name", "");
        ReflectionTestUtils.setField(user, "email", "a@a.ru");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserHasAllField() {
        UserRequestDto user = mock(UserRequestDto.class);
        ReflectionTestUtils.setField(user, "name", "name");
        ReflectionTestUtils.setField(user, "email", "a@a.ru");
        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}