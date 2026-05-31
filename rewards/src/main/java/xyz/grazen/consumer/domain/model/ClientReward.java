package xyz.grazen.rewards.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientReward(
                                UUID clientId,
                                String email,
                                Long points,
                                BigDecimal cashback,
                                OffsetDateTime createdAt) {
}
