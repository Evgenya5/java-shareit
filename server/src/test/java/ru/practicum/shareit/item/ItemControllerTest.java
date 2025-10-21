package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        ItemDto item1 = mock(ItemDto.class);
        ItemDto item2 = mock(ItemDto.class);
        Collection<ItemDto> items = Arrays.asList(item1, item2);

        when(itemService.findAll(1L)).thenReturn(items);

        Collection<ItemDto> result = itemController.findAll(1L);

        assertEquals(2, result.size());
        verify(itemService, times(1)).findAll(1L);
    }

    @Test
    void findById() {
        ItemDto item = mock(ItemDto.class);

        when(itemService.findById(1L)).thenReturn(item);
        ItemDto result = itemController.findById(1L);
        assertEquals(result, item);
        verify(itemService, times(1)).findById(1L);

    }

    @Test
    void searchByText() {
        ItemDto item1 = mock(ItemDto.class);
        ItemDto item2 = mock(ItemDto.class);

        Collection<ItemDto> items = Arrays.asList(item1, item2);

        when(itemService.searchByText("text")).thenReturn(items);

        Collection<ItemDto> result = itemController.searchByText("text");

        assertEquals(2, result.size());
        verify(itemService, times(1)).searchByText("text");
    }

    @Test
    void create() {
        ItemDto item = mock(ItemDto.class);
        CreateItemDto createItemDto = mock(CreateItemDto.class);

        when(itemService.create(1L, createItemDto)).thenReturn(item);

        ItemDto result = itemController.create(1L, createItemDto);

        assertEquals(item, result);
        verify(itemService, times(1)).create(1L, createItemDto);
    }

    @Test
    void createComment() {
        CommentDto commentDto = mock(CommentDto.class);

        when(itemService.createComment(1L, 1L, commentDto)).thenReturn(commentDto);

        CommentDto result = itemController.createComment(1L, 1L, commentDto);

        assertEquals(commentDto, result);
        verify(itemService, times(1)).createComment(1L, 1L, commentDto);
    }

    @Test
    void update() {
        ItemDto item = mock(ItemDto.class);
        CreateItemDto createItemDto = mock(CreateItemDto.class);

        when(itemService.update(1L, item, 1L)).thenReturn(item);

        ItemDto result = itemController.update(1L, 1L, item);

        assertEquals(item, result);
        verify(itemService, times(1)).update(1L, item, 1L);

    }

    @Test
    void delete() {
        itemController.delete(1L);
        verify(itemService, times(1)).delete(1L);
    }
}