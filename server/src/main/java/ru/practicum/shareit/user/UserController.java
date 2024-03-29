package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j(topic = "UserController")
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    public UserDto create(@RequestBody UserDto user) {
        log.info(String.format("Create User request. Data: %s", user));
        return mapper.toUserDto(userService.create(mapper.toUser(user)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto user) {
        log.info(String.format("Update User with id: %d . Data: %s", id, user));
        return mapper.toUserDto(userService.update(user, id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Get all Users");
        return mapper.toUserDtoList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDto getUserBy(@PathVariable("id") Long id) {
        log.info(String.format("Get User by Id: %d", id));
        return mapper.toUserDto(userService.getUserBy(id));
    }


    @DeleteMapping("/{id}")
    public void deleteUserBy(@PathVariable("id") Long id) {
        userService.deleteUserBy(id);
        log.info(String.format("Remove User with id: %d", id));
    }
}

