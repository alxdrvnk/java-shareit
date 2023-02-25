package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User create(User user);

    int update(User user);

    List<User> getAll();

    Optional<User> getBy(Long id);

    int deleteBy(Long id);
}
