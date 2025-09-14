package ru.practicum.shareit.user.dto;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Component
@Qualifier("inMemoryUserRepository")
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User findById(long id) {
        return Optional.ofNullable(users.get(id)).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    @Override
    public boolean checkDuplicateEmail(String email) {
        for (User user:users.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User update(User user) {
        Optional.ofNullable(user).orElseThrow(() ->
                new NotFoundException("Пользователь пустой"));
        Optional.ofNullable(users.get(user.getId())).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + user.getId() + " не найден"));
        return users.replace(user.getId(), user);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }
}