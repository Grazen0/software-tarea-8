package xyz.grazen.restaurant.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;

public record OrderResponseDto(
                UUID id,
                UUID clientId,
                String restaurantCode,
                BigDecimal total,
                OffsetDateTime createdAt) {

        public OrderResponseDto(Order order) {
                this(order.id(), order.clientId(), order.restaurantCode(), order.total(), order.createdAt());
        }
}
