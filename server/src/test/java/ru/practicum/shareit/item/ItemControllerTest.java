package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemControllerTest {

   /* @Mock
    private UserService userService;
*/
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        ItemDto item1 = mock(ItemDto.class);
        ItemDto item2 = mock(ItemDto.class);
        Collection<ItemDto> items = Arrays.asList(item1, item2);

        when(itemService.findAll(1L)).thenReturn(items);

        Collection<ItemDto> result = itemController.findAll(1L);

        assertEquals(2, result.size());
        verify(itemService, times(1)).findAll(1L);
    }

    @Test
    void findById() {

    }

    @Test
    void searchByText() {

    }

    @Test
    void create() {

    }

    @Test
    void createComment() {

    }

    @Test
    void update() {

    }

    @Test
    void delete() {

    }
}