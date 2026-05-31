package xyz.grazen.rewards.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import xyz.grazen.rewards.domain.model.ClientReward;

public interface ClientRewardService {

    public List<ClientReward> getAllRewards();

    public Optional<ClientReward> getRewardByClientId(UUID clientId);

    public void emitReward(UUID clientId, BigDecimal total, String cardId, String restaurantCode,
            OffsetDateTime createdAt);

    public Optional<ClientReward> updateRewardEmailById(UUID clientId, String email);

}
