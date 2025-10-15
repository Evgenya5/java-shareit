package ru.practicum.shareit.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    private Validator validator;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void itemCreate() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.itemCreate(1L, item)).thenReturn(response);
        ResponseEntity<Object> result = itemController.itemCreate(1L, item);
        assertEquals(response, result);
        verify(itemClient, times(1)).itemCreate(1L, item);
    }

    @Test
    void itemCreateNotValid() {
        ItemRequestDto item = new ItemRequestDto();
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.itemCreate(1L, item)).thenReturn(response);
        var violations = validator.validate(item);
        assertFalse(violations.isEmpty());
        ResponseEntity<Object> result = itemController.itemCreate(1L, item);
        assertEquals(response, result);
        verify(itemClient, times(1)).itemCreate(1L, item);
    }

    @Test
    void createComment() {
        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(commentRequestDto, HttpStatusCode.valueOf(200));
        when(itemClient.createComment(1L, 1L, commentRequestDto)).thenReturn(response);
        ResponseEntity<Object> result = itemController.createComment(1L, 1L, commentRequestDto);
        assertEquals(response, result);
        verify(itemClient, times(1)).createComment(1L, 1L, commentRequestDto);
    }

    @Test
    void createCommentNotExistItemId() {
        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(new Object(), HttpStatusCode.valueOf(404));
        when(itemClient.createComment(1L, 10000L, commentRequestDto)).thenReturn(response);
        ResponseEntity<Object> result = itemController.createComment(1L, 10000L, commentRequestDto);
        assertEquals(response, result);
        verify(itemClient, times(1)).createComment(1L, 10000L, commentRequestDto);
    }

    @Test
    void findById() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.getItem(1L)).thenReturn(response);
        ResponseEntity<Object> result = itemController.findById(1L);
        assertEquals(response, result);
        verify(itemClient, times(1)).getItem(1L);
    }

    @Test
    void findByIdNotExist() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(new Object(), HttpStatusCode.valueOf(404));
        when(itemClient.getItem(100000L)).thenReturn(response);
        ResponseEntity<Object> result = itemController.findById(100000L);
        assertEquals(response, result);
        verify(itemClient, times(1)).getItem(100000L);
    }

    @Test
    void searchByText() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ItemRequestDto item1 = mock(ItemRequestDto.class);
        Collection<ItemRequestDto> items = Arrays.asList(item, item1);
        ResponseEntity<Object> response = new ResponseEntity<>(items, HttpStatusCode.valueOf(200));
        when(itemClient.searchItemsByText("text")).thenReturn(response);
        ResponseEntity<Object> result = itemController.searchByText("text");
        assertEquals(response, result);
        verify(itemClient, times(1)).searchItemsByText("text");
    }

    @Test
    void findAll() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ItemRequestDto item1 = mock(ItemRequestDto.class);
        Collection<ItemRequestDto> items = Arrays.asList(item, item1);
        ResponseEntity<Object> response = new ResponseEntity<>(items, HttpStatusCode.valueOf(200));
        when(itemClient.getItems(1L)).thenReturn(response);
        ResponseEntity<Object> result = itemController.findAll(1L);
        assertEquals(response, result);
        verify(itemClient, times(1)).getItems(1L);
    }

    @Test
    void delete() {
        itemController.delete(1L);
        verify(itemClient, times(1)).itemDelete(1L);
    }

    @Test
    void update() {
        ItemRequestDto item = mock(ItemRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(item, HttpStatusCode.valueOf(200));
        when(itemClient.itemUpdate(1L, 1L, item)).thenReturn(response);
        ResponseEntity<Object> result = itemController.update(1L, 1L, item);
        assertEquals(response, result);
        verify(itemClient, times(1)).itemUpdate(1L, 1L, item);
    }
}