package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CommentMapper {
    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comment toComment(CommentRequestDto dto, User user, Item item, LocalDateTime time) {
        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .item(item)
                .author(user)
                .created(time)
                .build();
    }

    public List<CommentResponseDto> toCommentDtoList(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }

        List<CommentResponseDto> list = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            list.add(toCommentResponseDto(comment));
        }

        return list;
    }
}
