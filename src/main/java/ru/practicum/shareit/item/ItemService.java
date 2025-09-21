package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto create(Long userId, ItemDto itemDto) {

        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        // формируем дополнительные данные
        itemDto.setId(getNextId());
        Item item = ItemMapper.toItem(itemDto,userId);
        itemRepository.create(item);
        log.debug("create item with id: {}", item.getId());
        return ItemMapper.toItemDto(item);
    }

    public void delete(Long itemId) {
        if (itemId == null) {
            log.error("user id empty");
            throw new ValidationException("Id должен быть указан");
        }
        itemRepository.delete(itemId);
    }

    public Collection<ItemDto> findAll(Long userId) {
        return itemRepository.findAll(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto findById(Long id) {
        return ItemMapper.toItemDto(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден")));
    }

    public Collection<ItemDto> searchByText(String text) {
        return itemRepository.searchByText(text).stream().map(ItemMapper::toItemDto).toList();
    }

    public ItemDto update(Long id, ItemDto itemDto, Long userId) {

        Item oldItem = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с id = " + id + " не найден"));;
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
        return ItemMapper.toItemDto(itemRepository.update(oldItem));
    }

    private long getNextId() {
        long currentMaxId = itemRepository.findAll(null)
                .stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
