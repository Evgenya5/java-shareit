package validateDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.request.dto.RequestDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testDescriptionEmpty() {
        RequestDto request = mock(RequestDto.class);
        ReflectionTestUtils.setField(request, "description", "");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testDescriptionNull() {
        RequestDto request = mock(RequestDto.class);
        ReflectionTestUtils.setField(request, "description", null);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testDescriptionNotNull() {
        RequestDto request = mock(RequestDto.class);
        ReflectionTestUtils.setField(request, "description", "description");
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

}