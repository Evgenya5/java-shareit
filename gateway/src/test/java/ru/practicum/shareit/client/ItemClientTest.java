package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemClientTest {

    @Mock
    ItemClient itemClient;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchItemsByText() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ItemRequestDto item1 = mock(ItemRequestDto.class);
        Collection<ItemRequestDto> items = Arrays.asList(item, item1);
        ResponseEntity<Object> response = new ResponseEntity<>(items, HttpStatusCode.valueOf(200));
        when(itemClient.searchItemsByText("text")).thenReturn(response);
        ResponseEntity<Object> result = itemClient.searchItemsByText("text");
        assertEquals(response, result);
        verify(itemClient, times(1)).searchItemsByText("text");
    }

    @Test
    void getItems() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ItemRequestDto item1 = mock(ItemRequestDto.class);
        Collection<ItemRequestDto> items = Arrays.asList(item, item1);
        ResponseEntity<Object> response = new ResponseEntity<>(items, HttpStatusCode.valueOf(200));
        when(itemClient.getItems(1L)).thenReturn(response);
        ResponseEntity<Object> result = itemClient.getItems(1L);
        assertEquals(response, result);
        verify(itemClient, times(1)).getItems(1L);

    }

    @Test
    void itemCreate() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.itemCreate(1L, item)).thenReturn(response);
        ResponseEntity<Object> result = itemClient.itemCreate(1L, item);
        assertEquals(response, result);
        verify(itemClient, times(1)).itemCreate(1L, item);

    }

    @Test
    void createComment() {
        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(commentRequestDto, HttpStatusCode.valueOf(200));
        when(itemClient.createComment(1L, 1L, commentRequestDto)).thenReturn(response);
        ResponseEntity<Object> result = itemClient.createComment(1L, 1L, commentRequestDto);
        assertEquals(response, result);
        verify(itemClient, times(1)).createComment(1L, 1L, commentRequestDto);

    }

    @Test
    void getItem() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.getItem(1L)).thenReturn(response);
        ResponseEntity<Object> result = itemClient.getItem(1L);
        assertEquals(response, result);
        verify(itemClient, times(1)).getItem(1L);
    }

    @Test
    void itemUpdate() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.itemUpdate(1L, 1L, item)).thenReturn(response);
        ResponseEntity<Object> result = itemClient.itemUpdate(1L, 1L, item);
        assertEquals(response, result);
        verify(itemClient, times(1)).itemUpdate(1L, 1L, item);
    }

    @Test
    void itemDelete() {
        itemClient.itemDelete(1L);
        verify(itemClient, times(1)).itemDelete(1L);

    }
}