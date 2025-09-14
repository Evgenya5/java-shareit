package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    void delete(Long id);

    User findById(long id);

    boolean checkDuplicateEmail(String email);
}
