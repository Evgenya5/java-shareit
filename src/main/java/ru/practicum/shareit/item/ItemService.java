package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

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
        if (bookingRepository.findByBooker_IdAndItem_IdAndEndIsBefore(userId, id, LocalDateTime.now()).isEmpty()) {
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
        List<ItemDto> itemsDto = itemRepository.findByOwner(userId).stream().map(ItemMapper::toItemDto).toList();
        itemsDto.forEach(itemDto -> {
            itemDto.setComments(commentRepository.findByItemId(itemDto.getId()).stream().map(Comment::getText).toList());
            List<BookingDto> bookings = bookingRepository.findByItem_IdAndEndIsBeforeOrderByStartDesc(itemDto.getId(), LocalDateTime.now()).stream().map(BookingMapper::toBookingDto).toList();
            if (!bookings.isEmpty()) {
                itemDto.setLastBooking(bookings.getLast());
            }
            bookings = bookingRepository.findByItem_IdAndStartIsAfterOrderByStartDesc(itemDto.getId(), LocalDateTime.now()).stream().map(BookingMapper::toBookingDto).toList();
            if (!bookings.isEmpty()) {
                itemDto.setNextBooking(bookings.getFirst());
            }
        });
        return itemsDto;
    }

    public ItemDto findById(Long id) {
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден")));
        itemDto.setComments(commentRepository.findByItemId(id).stream().map(Comment::getText).toList());
        itemDto.setBookings(bookingRepository.findByItem_IdOrderByStartDesc(itemDto.getId()).stream().map(BookingMapper::toBookingDto).toList());
        return itemDto;
    }

    public Collection<ItemDto> searchByText(String text) {
        return itemRepository.findByText(text).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto update(Long id, ItemDto itemDto, Long userId) {

        Item oldItem = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден"));
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