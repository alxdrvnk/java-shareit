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

    private final HashMap<Long, User> users = new HashMap<>();

    private Long id = 1L;

    private Long getNextId(){
        return id++;
    }

    public User create(User user) {
        if (checkUniqueEmail(user.getEmail())) {
            Long id = getNextId();
            User newUser = user.withId(id);
            users.put(id, newUser);
            return newUser;
        } else {
            throw new ShareItValidationException("Duplicate email");
        }
    }

    public int update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return 1;
        } else {
           return 0;
        }
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<Object> getBy(Long id) {
        try {
            return Optional.of(users.get(id));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public int deleteBy(Long id) {
        return users.remove(id) == null ?  0 : 1;
    }

    private boolean checkUniqueEmail(String email) {
        return users.values().stream().filter(
                user -> user.getEmail().equals(email)).findAny().isEmpty();
    }
}
