package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public ItemDto create(Long userId, ItemDto itemDto) {

        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Item item = ItemMapper.toItem(itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public CommentDto createComment(Long userId, Long id, CommentDto commentDto) {

        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найдена"));
        if (bookingRepository.findByBooker_IdAndItem_IdAndEndIsBefore(userId, id, Instant.parse(LocalDateTime.now().toString() + "Z")).isEmpty()) {
            throw new ValidationException("Пользователь не имеет законченных бронирований данной вещи");
        }
        commentDto.setItemId(id);
        commentDto.setAuthorId(userId);
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, author));
        return CommentMapper.toCommentDto(comment);
    }

    public void delete(Long itemId) {
        if (itemId == null) {
            log.error("user id empty");
            throw new ValidationException("Id должен быть указан");
        }
        itemRepository.delete(itemRepository.findById(itemId).get());
    }

    public Collection<ItemDto> findAll(Long userId) {
        return itemRepository.findByOwner(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto findById(Long id) {
        return ItemMapper.toItemDto(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден")));
    }

    public Collection<ItemDto> searchByText(String text) {
        return itemRepository.findByText(text).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto update(Long id, ItemDto itemDto, Long userId) {

        Item oldItem = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден"));
        ;
        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        if (userId != null) {
            userRepository.findById(userId).orElseThrow(() ->
                    new NotFoundException("Пользователь с id = " + userId + " не найден"));
            oldItem.setOwner(userId);
        }
        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }
}