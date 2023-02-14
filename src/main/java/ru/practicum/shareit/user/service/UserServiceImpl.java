package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final InMemoryUserStorage storage;

    @Override
    public User create(User user) {
        return storage.create(user);
    }

    @Override
    public User update(User user) {
        if (storage.update(user) == 0) {
                throw new ShareItAlreadyExistsException(
                        String.format("User with email:%s already exists", user.getEmail()));
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAll();
    }

    @Override
    public User getUserBy(long id) {
        return (User) storage.getBy(id).orElseThrow(
                () -> new ShareItNotFoundException(String.format("User with id: %d not found", id)));
    }

    @Override
    public void deleteUserBy(long id) {
        if (storage.deleteBy(id) == 0) {
            throw new ShareItNotFoundException(
                    String.format("User with id: %d not found", id));
        }
    }

}
