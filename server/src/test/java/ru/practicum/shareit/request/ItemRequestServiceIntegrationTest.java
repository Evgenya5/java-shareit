package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User user;
    private Item availableItem;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        request = new ItemRequest();
        request.setDescription("description");
        itemRequestRepository.save(request);

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        owner = userRepository.save(owner);

        user = new User();
        user.setName("Name");
        user.setEmail("user@test.com");
        user = userRepository.save(user);

        availableItem = new Item();
        availableItem.setName("Available Item");
        availableItem.setDescription("Test available item");
        availableItem.setAvailable(true);
        availableItem.setOwner(owner.getId());
        availableItem = itemRepository.save(availableItem);
    }

    @Test
    void create() {
        CreateItemRequestDto requestDto = new CreateItemRequestDto();
        requestDto.setDescription("testDesc");
        ItemRequestDto result = itemRequestService.create(requestDto, user.getId());

        assertNotNull(result);
        assertNotNull(result.getCreated());
        assertEquals(user.getId(), result.getRequestor().getId());
        assertEquals(requestDto.getDescription(), result.getDescription());
        assertNotNull(result.getId());
    }

    @Test
    void findById() {
        ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);
        ItemRequestDto result = itemRequestService.findById(user.getId(), request.getId());
        assertNotNull(result);
        assertEquals(result.getDescription(), requestDto.getDescription());
        assertEquals(result.getCreated(), requestDto.getCreated());
        assertEquals(result.getRequestor(), requestDto.getRequestor());
        availableItem.setRequest(request.getId());
        itemRepository.save(availableItem);
        result = itemRequestService.findById(user.getId(), request.getId());
        assertNotNull(result);
        assertEquals(result.getItems().size(), 1);
        assertEquals(result.getItems().getFirst().getId(), availableItem.getId());
    }

    @Test
    void findAll() {
        ItemRequest request1 = new ItemRequest();
        request1.setDescription("description1");
        itemRequestRepository.save(request1);
        Collection<ItemRequestDto> result = itemRequestService.findAll(user.getId());
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }
}