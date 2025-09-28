package ru.practicum.shareit.booking.model;

import lombok.Getter;

@Getter
public enum State {
    ALL("Все"),
    CURRENT("Текущие"),
    PAST("Завершенные"),
    FUTURE("Будушие"),
    REJECTED("Ожидабщие подтверждения"),
    WAITING("Отклоненные");

    private final String description;

    State(String description) {
        this.description = description;
    }
}