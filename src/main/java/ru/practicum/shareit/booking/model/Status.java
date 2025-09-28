package ru.practicum.shareit.booking.model;
import lombok.Getter;

@Getter
public enum Status {
    WAITING("Новое бронирование, ожидает одобрения"),
    APPROVED("Бронирование подтверждено владельцем"),
    REJECTED("Бронирование отклонено владельцем"),
    CANCELED("Бронирование отменено создателем");

    private final String description;

    Status(String description) {
        this.description = description;
    }
}