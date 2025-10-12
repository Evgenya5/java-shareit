package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody CreateItemRequestDto createItemRequestDto) {
        return itemRequestService.create(createItemRequestDto, userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long id) {
        return itemRequestService.findById(userId, id);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findAllByUser(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findAll(userId);
    }
}
