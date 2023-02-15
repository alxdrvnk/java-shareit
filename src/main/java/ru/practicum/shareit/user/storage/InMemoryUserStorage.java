package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ShareItValidationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage {

    private final HashMap<String, User> users = new HashMap<>();

    // Понимаю что получиться дорого по памяти,
    // но пока что не зеаю как сделать быстрый поиск уникального email
    private final HashMap<Long, String> idMailMap = new HashMap<>();

    private long id = 1L;

    private Long getNextId() {
        return id++;
    }

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

    /**
     * TODO Make less if-else.
     */
    public int update(User user) {
        String email = idMailMap.get(user.getId());

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
                return  0;
            }
        }
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<Object> getBy(Long id) {
        try {
            return Optional.of(users.get(idMailMap.get(id)));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

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
