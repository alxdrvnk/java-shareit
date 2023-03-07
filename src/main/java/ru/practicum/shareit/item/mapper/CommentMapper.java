package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Mapper
public interface CommentMapper {

    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id", ignore = true)
    Comment toComment(CommentRequestDto dto, User author, Item item, LocalDateTime created);


    @Mapping(target = "authorName", source = "author.name")
    CommentResponseDto toCommentResponseDto(Comment comment);
}
