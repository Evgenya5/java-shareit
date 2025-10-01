package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto findById(long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден")));
    }

    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    public UserDto create(UserDto userDto) {

        // формируем дополнительные данные
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public UserDto update(Long userId, UserDto userDto) {

        User oldUser = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден"));
        // если найдена и все условия соблюдены, обновляем её содержимое

        if (userDto.getEmail() != null) {
            if (userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
                log.error("not valid email");
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            }
            oldUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            oldUser.setName(userDto.getName());
        }
        log.debug("update user with id: {}", oldUser.getId());
        userRepository.save(oldUser);
        return UserMapper.toUserDto(oldUser);
    }

    public void delete(Long userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + " не найден")));
    }
}