package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        CreateItemRequestDto createRequest = mock(CreateItemRequestDto.class);
        ItemRequestDto request = mock(ItemRequestDto.class);
        when(itemRequestService.create(createRequest, 1L)).thenReturn(request);
        ItemRequestDto result = itemRequestController.create(1L, createRequest);
        assertEquals(request, result);
        verify(itemRequestService, times(1)).create(createRequest, 1L);
    }

    @Test
    void findById() {
        ItemRequestDto request = mock(ItemRequestDto.class);
        when(itemRequestService.findById(1L, 1L)).thenReturn(request);
        ItemRequestDto result = itemRequestController.findById(1L, 1L);
        assertEquals(request, result);
        verify(itemRequestService, times(1)).findById(1L, 1L);
    }

    @Test
    void findAllByUser() {
        ItemRequestDto request1 = mock(ItemRequestDto.class);
        ItemRequestDto request2 = mock(ItemRequestDto.class);
        Collection<ItemRequestDto> requests = Arrays.asList(request1, request2);
        when(itemRequestService.findAllByUser(1L)).thenReturn(requests);
        Collection<ItemRequestDto> result = itemRequestController.findAllByUser(1L);
        assertEquals(requests, result);
        verify(itemRequestService, times(1)).findAllByUser(1L);

    }

    @Test
    void findAll() {
        ItemRequestDto request1 = mock(ItemRequestDto.class);
        ItemRequestDto request2 = mock(ItemRequestDto.class);
        Collection<ItemRequestDto> requests = Arrays.asList(request1, request2);
        when(itemRequestService.findAll(1L)).thenReturn(requests);
        Collection<ItemRequestDto> result = itemRequestController.findAll(1L);
        assertEquals(requests, result);
        verify(itemRequestService, times(1)).findAll(1L);
    }
}