package xyz.grazen.rewards.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import xyz.grazen.rewards.domain.model.ClientReward;

public record ClientRewardResponseDto(
                UUID clientId,
                String email,
                Long points,
                BigDecimal cashback,
                OffsetDateTime createdAt) {

        public ClientRewardResponseDto(ClientReward reward) {
                this(reward.clientId(), reward.email(), reward.points(), reward.cashback(), reward.createdAt());
        }
}
