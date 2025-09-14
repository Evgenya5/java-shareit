package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import java.util.Collection;

public interface ItemRepository {
    Collection<Item> findAll(Long userId);

    Collection<Item> searchByText(String text);

    Item create(Item item);

    void delete(Long id);

    Item findById(Long id);

    Item update(Item newItem);
}
