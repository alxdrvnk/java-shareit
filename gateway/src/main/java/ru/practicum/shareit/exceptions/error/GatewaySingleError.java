package ru.practicum.shareit.exceptions.error;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GatewaySingleError {
    int state;
    String error;
}
