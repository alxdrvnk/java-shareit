package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public User updateUserFromDto(UserDto userDto, User.UserBuilder user) {
        if (userDto == null) {
            return user.build();
        }

        if (userDto.getId() != null) {
            user.id(userDto.getId());
        }
        if (userDto.getName() != null) {
            user.name(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.email(userDto.getEmail());
        }

        return user.build();
    }

    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id(userDto.getId());
        user.name(userDto.getName());
        user.email(userDto.getEmail());

        return user.build();
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id(user.getId());
        userDto.name(user.getName());
        userDto.email(user.getEmail());

        return userDto.build();
    }

    public List<UserDto> toUserDtoList(List<User> userList) {
        if (userList == null) {
            return null;
        }

        List<UserDto> list = new ArrayList<>(userList.size());
        for (User user : userList) {
            list.add(toUserDto(user));
        }

        return list;
    }
}
