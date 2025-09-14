package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateEmailException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserRepository;
import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(long userId) {
        return userRepository.findById(userId);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        // проверяем выполнение необходимых условий
        validateUser(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        userRepository.create(user);
        log.debug("create user with id: {}", user.getId());
        return user;
    }

    public User update(Long userId, User user) {
        if (user == null || userId == null) {
            log.error("user id empty");
            throw new ValidationException("Id должен быть указан");
        }

        User oldUser = userRepository.findById(userId);
        // если найдена и все условия соблюдены, обновляем её содержимое

        if (user.getEmail() != null) {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.error("not valid email");
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            } else if (userRepository.checkDuplicateEmail(user.getEmail())) {
                throw new ValidateEmailException("электронная почта уже зарегистрирована");
            }
                oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        log.debug("update user with id: {}", oldUser.getId());
        userRepository.update(oldUser);
        return oldUser;
    }

    public void delete(Long userId) {
        if (userId == null) {
            log.error("user id empty");
            throw new ValidationException("Id должен быть указан");
        }
        userRepository.delete(userId);
    }

    private long getNextId() {
        long currentMaxId = userRepository.findAll()
                .stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateUser(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("empty email");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (userRepository.checkDuplicateEmail(user.getEmail())) {
            throw new ValidateEmailException("электронная почта уже зарегистрирована");
        }
    }
}