package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    void delete(Long id);

    Optional<User> findById(long id);

    boolean checkDuplicateEmail(String email);
}
