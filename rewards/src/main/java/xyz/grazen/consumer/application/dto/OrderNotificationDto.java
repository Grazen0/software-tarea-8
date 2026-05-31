package xyz.grazen.rewards.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderNotificationDto(
                UUID clientId,
                BigDecimal total,
                String cardId,
                String restaurantCode,
                OffsetDateTime createdAt) {
}
