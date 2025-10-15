package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class RequestControllerTest {

    @Mock
    private RequestClient requestClient;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestCreate() {
        RequestDto requestDto = mock(RequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(requestDto, HttpStatusCode.valueOf(200));
        when(requestClient.requestCreate(1L, requestDto)).thenReturn(response);
        ResponseEntity<Object> result = requestController.requestCreate(1L, requestDto);
        assertEquals(response, result);
        verify(requestClient, times(1)).requestCreate(1L, requestDto);
    }

    @Test
    void findById() {
        RequestDto requestDto = mock(RequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(requestDto, HttpStatusCode.valueOf(200));
        when(requestClient.getRequest(1L, 1L)).thenReturn(response);
        ResponseEntity<Object> result = requestController.findById(1L, 1L);
        assertEquals(response, result);
        verify(requestClient, times(1)).getRequest(1L, 1L);
    }

    @Test
    void findAllByUser() {
        RequestDto requestDto = mock(RequestDto.class);
        RequestDto requestDto1 = mock(RequestDto.class);
        Collection<RequestDto> reqs = Arrays.asList(requestDto, requestDto1);
        ResponseEntity<Object> response = new ResponseEntity<>(reqs, HttpStatusCode.valueOf(200));
        when(requestClient.getRequestsByUser(1L)).thenReturn(response);
        ResponseEntity<Object> result = requestController.findAllByUser(1L);
        assertEquals(response, result);
        verify(requestClient, times(1)).getRequestsByUser(1L);
    }

    @Test
    void findAll() {
        RequestDto requestDto = mock(RequestDto.class);
        RequestDto requestDto1 = mock(RequestDto.class);
        Collection<RequestDto> reqs = Arrays.asList(requestDto, requestDto1);
        ResponseEntity<Object> response = new ResponseEntity<>(reqs, HttpStatusCode.valueOf(200));
        when(requestClient.getAllRequests(1L)).thenReturn(response);
        ResponseEntity<Object> result = requestController.findAll(1L);
        assertEquals(response, result);
        verify(requestClient, times(1)).getAllRequests(1L);
    }
}