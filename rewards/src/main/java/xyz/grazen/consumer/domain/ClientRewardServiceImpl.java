package xyz.grazen.rewards.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.rewards.domain.model.ClientReward;
import xyz.grazen.rewards.domain.ports.ClientRewardRepository;

@Service
@RequiredArgsConstructor
public class ClientRewardServiceImpl implements ClientRewardService {

    private final ClientRewardRepository clientRewardRepository;

    @Value("${app.points.multiplier}")
    private BigDecimal pointsMultiplier;

    @Value("${app.cashback.factor}")
    private BigDecimal cashbackFactor;

    @Override
    public List<ClientReward> getAllRewards() {
        return clientRewardRepository.getAllClientRewards();
    }

    @Override
    public Optional<ClientReward> getRewardByClientId(UUID clientId) {
        return clientRewardRepository.getRewardByClientId(clientId);
    }

    @Override
    public void emitReward(UUID clientId, BigDecimal total, String cardId, String restaurantCode,
            OffsetDateTime createdAt) {
        long addedPoints = total.multiply(pointsMultiplier).longValue();
        BigDecimal addedCashback = total.multiply(cashbackFactor);
        clientRewardRepository.addBenefitsToClientById(clientId, addedPoints, addedCashback);

    }

    @Override
    public Optional<ClientReward> updateRewardEmailById(UUID clientId, String email) {
        return clientRewardRepository.updateRewardEmailById(clientId, email);
    }

}
