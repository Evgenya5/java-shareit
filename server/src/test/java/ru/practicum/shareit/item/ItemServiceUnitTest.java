package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void create() {

        CreateItemDto createDto = CreateItemDto.builder()
                .name("name")
                .description("description@mail.ru")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(createDto, 1L);
        ItemDto requestDto = ItemMapper.toItemDto(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto result = itemService.create(1L, createDto);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(itemRepository, times(1)).save(item);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void create_UserNotExist() {

        CreateItemDto createDto = CreateItemDto.builder()
                .name("name")
                .description("description@mail.ru")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(createDto, 1L);
        assertThrows(NotFoundException.class, () -> itemService.create(1L, createDto));
        verify(itemRepository, times(0)).save(item);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createComment() {
        Comment comment = new Comment();
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);
        comment.setText("text");
        comment.setItemId(item.getId());
        comment.setAuthor(user);
        comment.setCreated(LocalDate.now());
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(bookingRepository.findByBooker_IdAndItem_IdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(new Booking()));
        CommentDto result = itemService.createComment(1L, 1L, CommentMapper.toCommentDto(comment));
        assertNotNull(result);
        assertEquals(result, commentDto);
        verify(commentRepository, times(1)).save(comment);
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findByBooker_IdAndItem_IdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    }

    @Test
    void createComment_UserNotExist() {
        Comment comment = new Comment();
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);
        comment.setText("text");
        comment.setItemId(item.getId());
        comment.setAuthor(user);
        comment.setCreated(LocalDate.now());
        assertThrows(NotFoundException.class, () -> itemService.createComment(1L, 1L, CommentMapper.toCommentDto(comment)));
        verify(commentRepository, times(0)).save(comment);
        verify(itemRepository, times(0)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(bookingRepository, times(0)).findByBooker_IdAndItem_IdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    }

    @Test
    void createComment_ItemNotExist() {
        Comment comment = new Comment();
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);
        comment.setText("text");
        comment.setItemId(item.getId());
        comment.setAuthor(user);
        comment.setCreated(LocalDate.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class, () -> itemService.createComment(1L, 1L, CommentMapper.toCommentDto(comment)));
        verify(commentRepository, times(0)).save(comment);
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(bookingRepository, times(0)).findByBooker_IdAndItem_IdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    }

    @Test
    void createComment_BookingNotExist() {
        Comment comment = new Comment();
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setId(1L);
        comment.setText("text");
        comment.setItemId(item.getId());
        comment.setAuthor(user);
        comment.setCreated(LocalDate.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(ValidationException.class, () -> itemService.createComment(1L, 1L, CommentMapper.toCommentDto(comment)));
        verify(commentRepository, times(0)).save(comment);
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findByBooker_IdAndItem_IdAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    }

    @Test
    void delete() {
        Item item = new Item();
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        itemService.delete(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void findAll() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setOwner(1L);
        Item item2 = new Item();
        item2.setOwner(1L);
        item2.setId(2L);
        List<Item> items = Arrays.asList(item1, item2);
        when(itemRepository.findByOwner(1L)).thenReturn(items);
        List<Item> result = itemService.findAll(1L).stream().map(dto -> ItemMapper.toItem(dto,1L)).toList();
        assertNotNull(result);
        assertEquals(result, items);
        verify(itemRepository, times(1)).findByOwner(1L);
    }

    @Test
    void findById() {
        ItemDto requestDto = ItemDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .comments(List.of())
                .bookings(List.of())
                .build();

        Item item = ItemMapper.toItem(requestDto, 1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ItemDto result = itemService.findById(1L);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotExist() {
        assertThrows(NotFoundException.class, () -> itemService.findById(1L));
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void searchByText() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setOwner(1L);
        Item item2 = new Item();
        item2.setOwner(1L);
        item2.setId(2L);
        List<Item> items = Arrays.asList(item1, item2);
        when(itemRepository.findByText("text")).thenReturn(items);
        List<Item> result = itemService.searchByText("text").stream().map(dto -> ItemMapper.toItem(dto,1L)).toList();
        assertNotNull(result);
        assertEquals(result, items);
        verify(itemRepository, times(1)).findByText("text");
    }

    @Test
    void update() {

        ItemDto createDto = ItemDto.builder()
                .name("name")
                .description("description@mail.ru")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(createDto, 1L);
        ItemDto requestDto = ItemMapper.toItemDto(item);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        ItemDto result = itemService.update(1L, createDto, 1L);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(itemRepository, times(1)).save(item);
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void update_NotExistUser() {

        ItemDto createDto = ItemDto.builder()
                .name("name")
                .description("description@mail.ru")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(createDto, 1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> itemService.update(1L, createDto, 1L));
        verify(itemRepository, times(0)).save(item);
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void update_NotExistItem() {

        ItemDto createDto = ItemDto.builder()
                .name("name")
                .description("description@mail.ru")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(createDto, 1L);
        assertThrows(NotFoundException.class, () -> itemService.update(1L, createDto, 1L));
        verify(itemRepository, times(0)).save(item);
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).findById(1L);
    }
}