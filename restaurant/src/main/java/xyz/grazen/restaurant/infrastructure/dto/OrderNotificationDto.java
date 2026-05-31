package xyz.grazen.restaurant.infrastructure.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;

public record OrderNotificationDto(
        UUID clientId,
        BigDecimal total,
        String cardId,
        String restaurantCode,
        OffsetDateTime createdAt) {

    public OrderNotificationDto(Order order, String cardId) {
        this(order.clientId(), order.total(), cardId, order.restaurantCode(), order.createdAt());
    }

}
