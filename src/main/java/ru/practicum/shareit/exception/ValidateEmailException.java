package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ValidateEmailException extends RuntimeException {
    public ValidateEmailException(String message) {
        super(message);
    }
}
