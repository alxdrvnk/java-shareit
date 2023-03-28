package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class ItemRequestCreationDto {
    @NotBlank
    @NotNull
    String description;
}
