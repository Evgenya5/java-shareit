package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById() {
        UserDto requestDto = UserDto.builder()
                .name("name")
                .email("test@mail.ru")
                .build();

        User user = UserMapper.toUser(requestDto);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto result = userService.findById(1L);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdNotExist() {
        assertThrows(NotFoundException.class, () -> userService.findById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findAll() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll().stream().map(UserMapper::toUser).toList();

        assertNotNull(result);
        assertEquals(result, users);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void create() {
        UserDto requestDto = UserDto.builder()
                .name("name")
                .email("test@mail.ru")
                .build();

        User user = UserMapper.toUser(requestDto);
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.create(requestDto);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByEmail("test@mail.ru");
    }

    @Test
    void update() {
        UserDto requestDto = UserDto.builder()
                .name("name")
                .email("test@mail.ru")
                .build();

        User user = UserMapper.toUser(requestDto);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.update(1L, requestDto);
        assertNotNull(result);
        assertEquals(result, requestDto);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByEmail("test@mail.ru");
    }

    @Test
    void updateNotFound() {
        UserDto requestDto = UserDto.builder()
                .name("name")
                .email("test@mail.ru")
                .build();
        assertThrows(NotFoundException.class, () -> userService.update(1L, requestDto));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(Mockito.any(User.class));
    }

    @Test
    void delete() {
        User user = new User(1L, "name", "a@a.ru");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.delete(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> userService.delete(Mockito.anyLong()));
        verify(userRepository, times(0)).delete(Mockito.any());
    }
}