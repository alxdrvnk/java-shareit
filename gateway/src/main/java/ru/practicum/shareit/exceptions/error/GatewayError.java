package ru.practicum.shareit.exceptions.error;


import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GatewayError {
    int status;
    List<String> errors;
}
