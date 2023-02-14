package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toUserDto(User user) {

        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {

        if (userDto== null) {
            return null;
        }

        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static List<UserDto> toDtoList(List<User> allUsers) {
        return allUsers.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    //Наверное можно было бы использовать либу MapStruct, но это наверное читерство
    public static User patchUser(UserDto from, User to) {
        var user = User.builder()
                .id(to.getId())
                .name(to.getName())
                .email(to.getEmail());

        if (from.getName() != null) {
           user.name(from.getName());
        }

        if (from.getEmail() != null) {
            user.email(from.getEmail());
        }

        return user.build();
    }
}


