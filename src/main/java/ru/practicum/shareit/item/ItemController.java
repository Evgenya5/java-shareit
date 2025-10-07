package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByText(@RequestParam String text) {
        return itemService.searchByText(text);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, id, commentDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestBody ItemDto itemDto) {
        return itemService.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }
}
