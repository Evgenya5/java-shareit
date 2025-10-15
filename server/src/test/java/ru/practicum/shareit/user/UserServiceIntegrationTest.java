package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        user = new User();
        user.setName("User");
        user.setEmail("user@test.com");
        user = userRepository.save(user);
    }

    @Test
    void create() {
        UserDto userDto = UserDto.builder()
                .name("name")
                .email("a@a.ru")
                .build();
        UserDto result = userService.create(userDto);
        assertNotNull(result);
        assertEquals(result.getEmail(), userDto.getEmail());
        assertEquals(result.getName(), userDto.getName());
        assertNotNull(result.getId());
    }

    @Test
    void update() {
        UserDto userDto = UserMapper.toUserDto(user);
        userDto.setName("newName");
        userDto.setEmail("newemail@a.ru");
        UserDto result = userService.update(user.getId(), userDto);
        assertNotNull(result);
        assertEquals(result.getEmail(), userDto.getEmail());
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getId(), userDto.getId());
    }

    @Test
    void findById() {
        UserDto userDto = UserMapper.toUserDto(user);
        UserDto result = userService.findById(user.getId());
        assertNotNull(result);
        assertEquals(result.getEmail(), userDto.getEmail());
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getId(), userDto.getId());
    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setName("name2");
        user1.setEmail("email@a.ru");
        userRepository.save(user1);
        Collection<UserDto> result = userService.findAll();
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }
}