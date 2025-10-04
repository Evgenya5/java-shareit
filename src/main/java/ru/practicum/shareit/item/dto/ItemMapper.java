package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ArrayList<>(),
                new ArrayList<>(),
                null,
                null
        );
    }

    public static Item toItem(ItemDto itemDto, Long userId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                ""
        );
    }
}