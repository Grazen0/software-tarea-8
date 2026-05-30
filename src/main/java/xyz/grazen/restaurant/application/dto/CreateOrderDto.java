package xyz.grazen.restaurant.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderDto(
        @NotNull UUID clientId,
        @NotNull @NotEmpty String restaurantCode,
        @NotNull @Positive BigDecimal total,
        @NotNull @NotEmpty String cardId) {
}
