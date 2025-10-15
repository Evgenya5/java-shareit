package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserControllerTest {
    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        UserRequestDto user1 = mock(UserRequestDto.class);
        UserRequestDto user2 = mock(UserRequestDto.class);
        Collection<UserRequestDto> users = Arrays.asList(user1, user2);
        ResponseEntity<Object> response = new ResponseEntity<>(users, HttpStatusCode.valueOf(200));
        when(userClient.getUsers()).thenReturn(response);
        ResponseEntity<Object> result = userController.getUsers();
        assertEquals(response, result);
        verify(userClient, times(1)).getUsers();
    }

    @Test
    void findById() {
        UserRequestDto user = mock(UserRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        when(userClient.getUser(1L)).thenReturn(response);
        ResponseEntity<Object> result = userController.getUser(1L);
        assertEquals(response, result);
        verify(userClient, times(1)).getUser(1L);
    }

    @Test
    void create() {
        UserRequestDto user = mock(UserRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        when(userClient.userCreate(user)).thenReturn(response);
        ResponseEntity<Object> result = userController.userCreate(user);
        assertEquals(response, result);
        verify(userClient, times(1)).userCreate(user);
    }

    @Test
    void update() {
        UserRequestDto user = mock(UserRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        when(userClient.userUpdate(1L, user)).thenReturn(response);
        ResponseEntity<Object> result = userController.update(1L, user);
        assertEquals(response, result);
        verify(userClient, times(1)).userUpdate(1L, user);
    }

    @Test
    void delete() {
        userController.delete(1L);
        verify(userClient, times(1)).userDelete(1L);
    }

}