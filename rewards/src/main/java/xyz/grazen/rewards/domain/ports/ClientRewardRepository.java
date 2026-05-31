package xyz.grazen.rewards.domain.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import xyz.grazen.rewards.domain.model.ClientReward;

public interface ClientRewardRepository {

    public List<ClientReward> getAllClientRewards();

    public Optional<ClientReward> getRewardByClientId(UUID clientId);

    public ClientReward createClientReward(UUID clientId, String email);

    public ClientReward createClientReward(UUID clientId);

    public void addBenefitsToClientById(UUID clientId, long addedPoints, BigDecimal addedCashback);

    public Optional<ClientReward> updateRewardEmailById(UUID clientId, String email);

}
