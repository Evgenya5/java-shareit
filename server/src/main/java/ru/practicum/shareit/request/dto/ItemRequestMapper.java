package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(CreateItemRequestDto createItemRequestDto) {

        return new ItemRequest(
                null,
                null,
                createItemRequestDto.getDescription(),
                LocalDateTime.now()
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getRequestor(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                new ArrayList<>()
        );
    }
}
