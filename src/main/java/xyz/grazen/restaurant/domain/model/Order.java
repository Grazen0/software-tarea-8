package xyz.grazen.restaurant.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Order(
        UUID id,
        UUID clientId,
        String restaurantCode,
        OffsetDateTime createdAt) {
}
