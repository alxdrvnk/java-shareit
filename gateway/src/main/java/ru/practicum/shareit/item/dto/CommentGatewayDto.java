package ru.practicum.shareit.item.dto;


import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class CommentGatewayDto {
    long id;
    @NotBlank
    String text;
}
