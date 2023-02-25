package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ShareItValidationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserDao {

    private final HashMap<String, User> users;

    private final HashMap<Long, String> idMailMap;

    private long id = 1L;

    private Long getNextId() {
        return id++;
    }

    @Override
    public User create(User user) {

        if (!users.containsKey(user.getEmail())) {

            Long id = getNextId();
            User newUser = user.withId(id);

            users.put(user.getEmail(), newUser);
            idMailMap.put(id, user.getEmail());

            return newUser;
        } else {
            throw new ShareItValidationException(
                    String.format("User with email: %s already exists", user.getEmail()));
        }
    }

    @Override
    public int update(User user) {
        String email = idMailMap.get(user.getId());
        try {
            if (email.equals(user.getEmail())) {
                users.put(email, user);
                return 1;
            } else {
                if (!idMailMap.containsValue(user.getEmail())) {
                    idMailMap.put(user.getId(), user.getEmail());
                    users.remove(email);
                    users.put(user.getEmail(), user);
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getBy(Long id) {
        try {
            return Optional.of(users.get(idMailMap.get(id)));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public int deleteBy(Long id) {
        String email = idMailMap.get(id);

        if (email != null) {
            users.remove(email);
            idMailMap.remove(id);
            return 1;
        }
        return 0;
    }
}
