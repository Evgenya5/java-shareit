package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequestDto findById(Long userId, Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Запрос с id = " + id + " не найден"));
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        List<ItemForRequestDto> itemDtos = itemRepository.findByRequest(id).stream().map(ItemMapper::toItemForReqDto).toList();
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemDtos);
        return itemRequestDto;
    }

    public Collection<ItemRequestDto> findAllByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Map<Long, ItemRequestDto> itemReqMap = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toMap(ItemRequestDto::getId, Function.identity()));
        Map<Long, List<Item>> itemMap = itemRepository.findByRequest_Ids(itemReqMap.keySet())
                .stream()
                .collect(Collectors.groupingBy(Item::getRequest));
        return itemReqMap.values()
                .stream()
                .map(itemReqDto -> makeItemReqWithItems(itemReqDto, itemMap.getOrDefault(itemReqDto.getId(), Collections.emptyList())))
                .toList();
    }

    public Collection<ItemRequestDto> findAll(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Map<Long, ItemRequestDto> itemReqMap = itemRequestRepository.findAll().stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toMap(ItemRequestDto::getId, Function.identity()));
        Map<Long, List<Item>> itemMap = itemRepository.findByRequest_Ids(itemReqMap.keySet())
                .stream()
                .collect(Collectors.groupingBy(Item::getRequest));
        return itemReqMap.values()
                .stream()
                .map(itemReqDto -> makeItemReqWithItems(itemReqDto, itemMap.getOrDefault(itemReqDto.getId(), Collections.emptyList())))
                .toList();
    }

    public ItemRequestDto create(CreateItemRequestDto createItemRequestDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(createItemRequestDto);
        itemRequest.setRequestor(user);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    private ItemRequestDto makeItemReqWithItems(ItemRequestDto itemReqDto, List<Item> items) {

        itemReqDto.setItems(items.stream().map(ItemMapper::toItemForReqDto).toList());
        return itemReqDto;
    }
}