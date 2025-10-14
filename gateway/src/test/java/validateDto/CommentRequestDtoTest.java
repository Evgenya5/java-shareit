package validateDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommentRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testTextNull() {
        CommentRequestDto comment = mock(CommentRequestDto.class);
        ReflectionTestUtils.setField(comment, "text", null);
        var violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testTextBlank() {
        CommentRequestDto comment = mock(CommentRequestDto.class);
        ReflectionTestUtils.setField(comment, "text", "");
        var violations = validator.validate(comment);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testTextNotNull() {
        CommentRequestDto comment = mock(CommentRequestDto.class);
        ReflectionTestUtils.setField(comment, "text", "1234");
        var violations = validator.validate(comment);
        assertTrue(violations.isEmpty());
    }
}