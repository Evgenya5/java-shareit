package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

/**
 * TODO Sprint add-controllers.
 */
@Component
@Qualifier("inMemoryItemRepository")
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> findAll(Long userId) {
        if (userId == null) {
            return items.values();
        }
        return items.values().stream().filter(item -> item.getOwner().equals(userId)).toList();
    }

    @Override
    public Collection<Item> searchByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return items.values().stream().filter(item -> (item.getDescription().toUpperCase().contains(text.toUpperCase()) || item.getName().toUpperCase().contains(text.toUpperCase())) && item.getAvailable().equals("true")).toList();
    }

    @Override
    public Item create(Item item) {
        items.put(item.getId(),item);
        return item;
    }

    public void delete(Long itemId) {
        items.remove(itemId);
    }

    @Override
    public Item findById(Long id) {

        return Optional.ofNullable(items.get(id)).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден"));
    }

    @Override
    public Item update(Item newItem) {
        Optional.ofNullable(newItem).orElseThrow(() ->
                new NotFoundException("Вещь пустая"));
        Optional.ofNullable(items.get(newItem.getId())).orElseThrow(() ->
                new NotFoundException("Вещь с id " + newItem.getId() + " не найдена"));
        return items.replace(newItem.getId(), newItem);
    }
}
