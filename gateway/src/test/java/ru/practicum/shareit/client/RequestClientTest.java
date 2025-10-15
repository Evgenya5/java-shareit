package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class RequestClientTest {

    @Mock
    RequestClient requestClient;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRequests() {
    }

    @Test
    void requestCreate() {
        RequestDto requestDto = mock(RequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(requestDto, HttpStatusCode.valueOf(200));
        when(requestClient.requestCreate(1L, requestDto)).thenReturn(response);
        ResponseEntity<Object> result = requestClient.requestCreate(1L, requestDto);
        assertEquals(response, result);
        verify(requestClient, times(1)).requestCreate(1L, requestDto);

    }

    @Test
    void getRequest() {
        RequestDto requestDto = mock(RequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(requestDto, HttpStatusCode.valueOf(200));
        when(requestClient.getRequest(1L, 1L)).thenReturn(response);
        ResponseEntity<Object> result = requestClient.getRequest(1L, 1L);
        assertEquals(response, result);
        verify(requestClient, times(1)).getRequest(1L, 1L);

    }

    @Test
    void getRequestsByUser() {
        RequestDto requestDto = mock(RequestDto.class);
        RequestDto requestDto1 = mock(RequestDto.class);
        Collection<RequestDto> reqs = Arrays.asList(requestDto, requestDto1);
        ResponseEntity<Object> response = new ResponseEntity<>(reqs, HttpStatusCode.valueOf(200));
        when(requestClient.getRequestsByUser(1L)).thenReturn(response);
        ResponseEntity<Object> result = requestClient.getRequestsByUser(1L);
        assertEquals(response, result);
        verify(requestClient, times(1)).getRequestsByUser(1L);

    }
}