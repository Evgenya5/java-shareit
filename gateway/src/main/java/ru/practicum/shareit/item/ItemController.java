package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
	private final ItemClient itemClient;

	@PostMapping
	public ResponseEntity<Object> itemCreate(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemRequestDto requestDto) {
		//log.info("Creating item {} for user {}", requestDto, userId);
		return itemClient.itemCreate(userId, requestDto);
	}

	@PostMapping("/{id}/comment")
	public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @Valid @RequestBody CommentRequestDto commentDto) {
		//log.info("add comment {} for item {} by userId={}", commentDto, id, userId);
		return itemClient.createComment(userId, id, commentDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		//log.info("Get item by Id={}", id);
		return itemClient.getItem(id);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchByText(@RequestParam String text) {
		//log.info("Get items by text={}", text);
		return itemClient.searchItemsByText(text);
	}

	@GetMapping
	public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
		//log.info("Get items by userId={}", userId);
		return itemClient.getItems(userId);
	}

	@DeleteMapping("/{itemId}")
	public ResponseEntity<Object> delete(@PathVariable Long itemId) {
		//log.info("Delete itemId={}", itemId);
		return itemClient.itemDelete(itemId);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestBody ItemRequestDto itemDto) {
		//log.info("Update item by userId={}, item with id = {}, body {}", userId, id, itemDto);
		return itemClient.itemUpdate(id,  userId, itemDto);
	}

}
