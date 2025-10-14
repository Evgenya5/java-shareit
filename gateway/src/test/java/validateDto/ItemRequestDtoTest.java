package validateDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ItemRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testItemHasAllFields() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", "name");
        ReflectionTestUtils.setField(item, "available", true);
        ReflectionTestUtils.setField(item, "description", "description");
        ReflectionTestUtils.setField(item, "requestId", 1L);
        var violations = validator.validate(item);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testItemNameEmpty() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", "");
        ReflectionTestUtils.setField(item, "available", true);
        ReflectionTestUtils.setField(item, "description", "description");
        ReflectionTestUtils.setField(item, "requestId", 1L);
        var violations = validator.validate(item);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testItemDescriptionEmpty() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", "name");
        ReflectionTestUtils.setField(item, "available", true);
        ReflectionTestUtils.setField(item, "description", "");
        ReflectionTestUtils.setField(item, "requestId", 1L);
        var violations = validator.validate(item);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testItemNameNull() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", null);
        ReflectionTestUtils.setField(item, "available", true);
        ReflectionTestUtils.setField(item, "description", "description");
        ReflectionTestUtils.setField(item, "requestId", 1L);
        var violations = validator.validate(item);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testItemDescriptionNull() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", "name");
        ReflectionTestUtils.setField(item, "available", true);
        ReflectionTestUtils.setField(item, "description", null);
        ReflectionTestUtils.setField(item, "requestId", 1L);
        var violations = validator.validate(item);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testItemAvailableNull() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", "name");
        ReflectionTestUtils.setField(item, "available", null);
        ReflectionTestUtils.setField(item, "description", "description");
        ReflectionTestUtils.setField(item, "requestId", 1L);
        var violations = validator.validate(item);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testItemRequestIdNull() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ReflectionTestUtils.setField(item, "name", "name");
        ReflectionTestUtils.setField(item, "available", true);
        ReflectionTestUtils.setField(item, "description", "description");
        ReflectionTestUtils.setField(item, "requestId", null);
        var violations = validator.validate(item);
        assertTrue(violations.isEmpty());
    }


}