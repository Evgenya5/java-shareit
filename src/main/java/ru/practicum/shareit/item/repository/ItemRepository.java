package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Collection<Item> findAll(Long userId);

    Collection<Item> searchByText(String text);

    Item create(Item item);

    void delete(Long id);

    Optional<Item> findById(Long id);

    Item update(Item newItem);
}
