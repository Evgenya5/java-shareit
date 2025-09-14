package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserRepository;

import java.util.Collection;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Item create(Long userId, Item item) {
        // проверяем выполнение необходимых условий
        if (userId == null) {
            log.error("user id empty");
            throw new ValidationException("user Id должен быть указан");
        }
        userRepository.findById(userId);
        validateItem(item);
        // формируем дополнительные данные
        item.setId(getNextId());
        item.setOwner(userId);
        itemRepository.create(item);
        log.debug("create item with id: {}", item.getId());
        return item;
    }

    public void delete(Long itemId) {
        if (itemId == null) {
            log.error("user id empty");
            throw new ValidationException("Id должен быть указан");
        }
        itemRepository.delete(itemId);
    }

    public Collection<Item> findAll(Long userId) {
        return itemRepository.findAll(userId);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id);
    }

    public Collection<Item> searchByText(String text) {
        return itemRepository.searchByText(text);
    }

    public Item update(Long id, Item item, Long userId) {
        if (item == null) {
            throw new ValidationException("item не может быть пустой");
        }
        Item oldItem = itemRepository.findById(id);
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        if (userId != null) {
            userRepository.findById(userId);
            oldItem.setOwner(userId);
        }
        return itemRepository.update(oldItem);
    }

    private long getNextId() {
        long currentMaxId = itemRepository.findAll(null)
                .stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateItem(Item item) {

        if (item.getName() == null || item.getName().isBlank()) {
            log.error("empty name");
            throw new ValidationException("name не может быть пустой");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.error("empty description");
            throw new ValidationException("description не может быть пустой");
        }
        if (item.getAvailable() == null || item.getAvailable().isBlank()) {
            log.error("empty available");
            throw new ValidationException("available не может быть пустой");
        }
    }
}
