package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<Item> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{id}")
    public Item findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping("/search")
    public Collection<Item> searchByText(@RequestParam String text) {
        return itemService.searchByText(text);
    }

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody Item item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("/{id}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestBody Item item) {
        return itemService.update(id, item, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }
}
