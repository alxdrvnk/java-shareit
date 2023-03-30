package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.GatewayValidationException;
import ru.practicum.shareit.user.dto.UserGatewayDto;

import javax.validation.constraints.PositiveOrZero;

@Slf4j(topic = "Gateway UserController")
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable(name = "id") long userId) {

        if (userId < 0) {
            throw new GatewayValidationException("User Id can't be negative");
        }

        log.info(String.format("Get user with id: %d", userId));
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated @RequestBody UserGatewayDto dto) {
        log.info(String.format("Create User request. Data: %s", dto));
        return userClient.addUser(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody UserGatewayDto dto) {
        log.info(String.format("Update User with id: %d . Data: %s", userId, dto));
        return userClient.updateUser(userId, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") long userId) {
        userClient.deleteUser(userId);
        log.info(String.format("Remove User with id: %d", userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}