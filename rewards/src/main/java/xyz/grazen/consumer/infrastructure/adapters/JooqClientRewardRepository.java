package xyz.grazen.rewards.infrastructure.adapters;

import static org.jooq.impl.DSL.asterisk;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.rewards.domain.model.ClientReward;
import xyz.grazen.rewards.domain.ports.ClientRewardRepository;
import xyz.grazen.rewards.infrastructure.jooq.public_.tables.ClientRewards;

@Service
@RequiredArgsConstructor
public class JooqClientRewardRepository implements ClientRewardRepository {

    private static final ClientRewards clientRewards = ClientRewards.CLIENT_REWARDS;

    private final DSLContext dsl;

    @Override
    public List<ClientReward> getAllClientRewards() {
        return dsl
                .select(asterisk())
                .from(clientRewards)
                .fetchInto(ClientReward.class);
    }

    @Override
    public Optional<ClientReward> getRewardByClientId(UUID clientId) {
        return dsl
                .select(asterisk())
                .from(clientRewards)
                .where(clientRewards.CLIENT_ID.eq(clientId))
                .fetchOptionalInto(ClientReward.class);
    }

    @Override
    public ClientReward createClientReward(UUID clientId, String email) {
        return dsl
                .insertInto(clientRewards)
                .set(clientRewards.CLIENT_ID, clientId)
                .set(clientRewards.EMAIL, email)
                .returning()
                .fetchOneInto(ClientReward.class);

    }

    @Override
    public ClientReward createClientReward(UUID clientId) {
        return dsl
                .insertInto(clientRewards)
                .set(clientRewards.CLIENT_ID, clientId)
                .returning()
                .fetchOneInto(ClientReward.class);

    }

    @Override
    public void addBenefitsToClientById(UUID clientId, long addedPoints, BigDecimal addedCashback) {
        dsl
                .insertInto(clientRewards)
                .set(clientRewards.CLIENT_ID, clientId)
                .set(clientRewards.POINTS, addedPoints)
                .set(clientRewards.CASHBACK, addedCashback)
                .onDuplicateKeyUpdate()
                .set(clientRewards.POINTS, clientRewards.POINTS.plus(addedPoints))
                .execute();
    }

    @Override
    public Optional<ClientReward> updateRewardEmailById(UUID clientId, String email) {
        return dsl
                .update(clientRewards)
                .set(clientRewards.EMAIL, email)
                .where(clientRewards.CLIENT_ID.eq(clientId))
                .returning()
                .fetchOptionalInto(ClientReward.class);
    }
}
