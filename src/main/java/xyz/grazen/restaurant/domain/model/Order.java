package xyz.grazen.restaurant.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record Order(
                UUID id,
                UUID clientId,
                String restaurantCode,
                BigDecimal total,
                OffsetDateTime createdAt) {
}
