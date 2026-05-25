package xyz.grazen.restaurant.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateOrderDto(
                @NotNull UUID clientId,
                @NotNull String restaurantCode) {
}
