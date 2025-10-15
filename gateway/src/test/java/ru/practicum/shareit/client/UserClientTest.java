package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserRequestDto;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock
    private UserClient userClient;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers() {
        UserRequestDto user1 = mock(UserRequestDto.class);
        UserRequestDto user2 = mock(UserRequestDto.class);
        Collection<UserRequestDto> users = Arrays.asList(user1, user2);
        ResponseEntity<Object> response = new ResponseEntity<>(users, HttpStatusCode.valueOf(200));
        when(userClient.getUsers()).thenReturn(response);
        ResponseEntity<Object> result = userClient.getUsers();
        assertEquals(response, result);
        verify(userClient, times(1)).getUsers();
    }

    @Test
    void getUser() {
        UserRequestDto user = mock(UserRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        when(userClient.getUser(1L)).thenReturn(response);
        ResponseEntity<Object> result = userClient.getUser(1L);
        assertEquals(response, result);
        verify(userClient, times(1)).getUser(1L);
    }

    @Test
    void userCreate() {
        UserRequestDto user = mock(UserRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        when(userClient.userCreate(user)).thenReturn(response);
        ResponseEntity<Object> result = userClient.userCreate(user);
        assertEquals(response, result);
        verify(userClient, times(1)).userCreate(user);
    }

    @Test
    void userUpdate() {
        UserRequestDto user = mock(UserRequestDto.class);
        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        when(userClient.userUpdate(1L, user)).thenReturn(response);
        ResponseEntity<Object> result = userClient.userUpdate(1L, user);
        assertEquals(response, result);
        verify(userClient, times(1)).userUpdate(1L, user);
    }

    @Test
    void delete() {
        userClient.userDelete(1L);
        verify(userClient, times(1)).userDelete(1L);
    }
}