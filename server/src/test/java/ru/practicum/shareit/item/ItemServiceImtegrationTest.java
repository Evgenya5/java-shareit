package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceImtegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private Item availableItem;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner = userRepository.save(owner);

        availableItem = new Item();
        availableItem.setName("Available Item");
        availableItem.setDescription("Test available item");
        availableItem.setAvailable(true);
        availableItem.setOwner(owner.getId());
        availableItem = itemRepository.save(availableItem);
    }

    @Test
    void create() {
        CreateItemDto itemDto = CreateItemDto.builder()
                .name("name")
                .available(true)
                .description("description")
                .build();

        ItemDto result = itemService.create(owner.getId(), itemDto);

        assertNotNull(result);
        assertEquals(result.getAvailable(), itemDto.getAvailable());
        assertEquals(result.getDescription(), itemDto.getDescription());
        assertEquals(result.getName(), itemDto.getName());
        assertNotNull(result.getId());
    }

    @Test
    void update() {
        availableItem.setName("newName");
        ItemDto result = itemService.update(availableItem.getId(), ItemMapper.toItemDto(availableItem), owner.getId());
        assertNotNull(result);
        assertEquals(result.getName(), availableItem.getName());
        assertEquals(result.getId(), availableItem.getId());
        assertEquals(result.getAvailable(), availableItem.getAvailable());
        assertEquals(result.getDescription(), availableItem.getDescription());
    }

    @Test
    void findById() {
        ItemDto result = itemService.findById(availableItem.getId());
        assertNotNull(result);
        assertEquals(result.getName(), availableItem.getName());
        assertEquals(result.getId(), availableItem.getId());
        assertEquals(result.getAvailable(), availableItem.getAvailable());
        assertEquals(result.getDescription(), availableItem.getDescription());
    }
}