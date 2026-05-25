package xyz.grazen.restaurant.domain;

import java.time.Instant;
import java.util.UUID;

public class Order {
    private UUID id;
    private UUID clientId;
    private String restaurantCode;
    private Instant createdAt;
}
