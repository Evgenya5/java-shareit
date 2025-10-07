package ru.practicum.shareit.booking.model;
import lombok.Getter;

@Getter
public enum BookingStatus {
    WAITING("Новое бронирование, ожидает одобрения"),
    APPROVED("Бронирование подтверждено владельцем"),
    REJECTED("Бронирование отклонено владельцем"),
    CANCELED("Бронирование отменено создателем");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}