package ru.practicum.shareit.booking.model;

import lombok.Getter;

@Getter
public enum BookingState {
    ALL("Все"),
    CURRENT("Текущие"),
    PAST("Завершенные"),
    FUTURE("Будущие"),
    REJECTED("Ожидающие подтверждения"),
    WAITING("Отклоненные");

    private final String description;

    BookingState(String description) {
        this.description = description;
    }
}