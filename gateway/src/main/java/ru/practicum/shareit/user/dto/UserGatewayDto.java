package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class UserGatewayDto {
    Long id;

    @NotNull(message = "Name не может быть null")
    @NotBlank(message = "Name не может быть пустым")
    String name;

    @NotNull(message = "Email не может быть null")
    @NotBlank(message = "Email не может быть пустым")
    @Email(regexp = "^[a-zA-Z0-9.]+[^._]@[^.\\-_]+[a-zA-Z0-9.]+[a-zA-Z0-9]$", message = "Email введен некорректно")
    String email;
}
