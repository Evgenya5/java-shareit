package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private  ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestService itemRequestService;

    @Test
    void findById() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        ItemRequestDto result = itemRequestService.findById(1L, 1L);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(itemRequestRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findByRequest(1L);
    }

    @Test
    void findById_RequestNotExist() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findById(1L, 1L));
        verify(itemRequestRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).findById(1L);
        verify(itemRepository, times(0)).findByRequest(1L);
    }

    @Test
    void findById_UserNotExist() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        assertThrows(NotFoundException.class, () -> itemRequestService.findById(1L, 1L));
        verify(itemRequestRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(0)).findByRequest(1L);
    }

    @Test
    void findAllByUser() {
        ItemRequest req1 = new ItemRequest();
        req1.setId(1L);
        ItemRequest req2 = new ItemRequest();
        req2.setId(2L);
        List<ItemRequest> reqs = Arrays.asList(req1, req2);
        when(itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(1L)).thenReturn(reqs);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        List<ItemRequestDto> result = itemRequestService.findAllByUser(1L).stream().toList();
        assertNotNull(result);
        assertEquals(result, Stream.of(req1, req2).map(ItemRequestMapper::toItemRequestDto).toList());
        verify(itemRequestRepository, times(1)).findByRequestor_IdOrderByCreatedDesc(1L);
        verify(userRepository, times(1)).findById(Mockito.any());
        verify(itemRepository, times(1)).findByRequest_Ids(Mockito.any());
    }

    @Test
    void findAllByUser_UserNotExist() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findAllByUser(1L));
        verify(itemRequestRepository, times(0)).findByRequestor_IdOrderByCreatedDesc(1L);
        verify(userRepository, times(1)).findById(Mockito.any());
        verify(itemRepository, times(0)).findByRequest_Ids(Mockito.any());
    }

    @Test
    void findAll() {
        ItemRequest req1 = new ItemRequest();
        req1.setId(1L);
        ItemRequest req2 = new ItemRequest();
        req2.setId(2L);
        List<ItemRequest> reqs = Arrays.asList(req1, req2);
        when(itemRequestRepository.findAll()).thenReturn(reqs);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        List<ItemRequestDto> result = itemRequestService.findAll(1L).stream().toList();
        assertNotNull(result);
        assertEquals(result, Stream.of(req1, req2).map(ItemRequestMapper::toItemRequestDto).toList());
        verify(itemRequestRepository, times(1)).findAll();
        verify(userRepository, times(1)).findById(Mockito.any());
        verify(itemRepository, times(1)).findByRequest_Ids(Mockito.any());
    }

    @Test
    void findAll_UserNotExist() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findAll(1L));
        verify(itemRequestRepository, times(0)).findAll();
        verify(userRepository, times(1)).findById(Mockito.any());
        verify(itemRepository, times(0)).findByRequest_Ids(Mockito.any());
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(createItemRequestDto);
        itemRequest.setId(1L);
        itemRequest.setRequestor(user);

        when(itemRequestRepository.save(Mockito.any())).thenReturn(itemRequest);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ItemRequestDto result = itemRequestService.create(createItemRequestDto, 1L);
        assertNotNull(result);
        assertEquals(result, ItemRequestMapper.toItemRequestDto(itemRequest));
        verify(itemRequestRepository, times(1)).save(Mockito.any());
        verify(userRepository, times(1)).findById(Mockito.any());
    }

    @Test
    void create_UserNotExist() {
        assertThrows(NotFoundException.class, () -> itemRequestService.create(new CreateItemRequestDto(), 1L));
        verify(itemRequestRepository, times(0)).save(Mockito.any());
        verify(userRepository, times(1)).findById(Mockito.any());
    }
}