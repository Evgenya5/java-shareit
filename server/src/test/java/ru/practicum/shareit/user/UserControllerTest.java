package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        UserDto user1 = mock(UserDto.class);
        UserDto user2 = mock(UserDto.class);
        Collection<UserDto> users = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(users);

        Collection<UserDto> result = userController.findAll();

        assertEquals(2, result.size());
        verify(userService, times(1)).findAll();
    }

    @Test
    void findById() {
        UserDto user = mock(UserDto.class);
        when(userService.findById(Mockito.anyLong())).thenReturn(user);
        UserDto result = userController.findById(1L);
        assertEquals(user, result);
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void create() {
        UserDto user = mock(UserDto.class);
        when(userService.create(user)).thenReturn(user);
        UserDto result = userController.create(user);
        assertEquals(user, result);
        verify(userService, times(1)).create(user);
    }

    @Test
    void update() {
        UserDto user = mock(UserDto.class);
        when(userService.update(1L, user)).thenReturn(user);
        UserDto result = userController.update(1L, user);
        assertEquals(user, result);
        verify(userService, times(1)).update(1L, user);
    }

    @Test
    void delete() {
        userController.delete(1L);
        verify(userService, times(1)).delete(1L);
    }
}