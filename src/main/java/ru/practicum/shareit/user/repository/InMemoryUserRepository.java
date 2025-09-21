package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Qualifier("inMemoryUserRepository")
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
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
        return users.replace(user.getId(), user);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }
}