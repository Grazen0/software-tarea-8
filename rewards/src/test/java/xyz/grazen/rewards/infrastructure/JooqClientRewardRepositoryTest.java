package xyz.grazen.rewards.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.jooq.DSLContext;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.grazen.rewards.TestcontainersConfiguration;
import xyz.grazen.rewards.infrastructure.adapters.JooqClientRewardRepository;
import xyz.grazen.rewards.infrastructure.jooq.public_.tables.ClientRewards;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@Testcontainers
public class JooqClientRewardRepositoryTest {

    @Autowired
    JooqClientRewardRepository clientRewardRepository;

    @Autowired
    DSLContext dsl;

    @BeforeEach
    void setUp() {
        dsl.truncate(ClientRewards.CLIENT_REWARDS).execute();
    }

    @Test
    public void shouldReturnEmptyListInitially() {
        var orders = clientRewardRepository.getAllClientRewards();
        assertNotNull(orders);
        assertEquals(0, orders.size());
    }

    @Test
    public void shouldCreateClientRewardWithAllFields() {
        var clientId = UUID.randomUUID();
        var created = clientRewardRepository.createClientReward(clientId, "test@example.com");

        assertNotNull(created);
        assertEquals(clientId, created.clientId());
        assertEquals("test@example.com", created.email());
        assertNotNull(created.createdAt());
    }

    @Test
    public void shouldCreateClientRewardWithOnlyClientId() {
        var clientId = UUID.randomUUID();
        var created = clientRewardRepository.createClientReward(clientId);

        assertNotNull(created);
        assertEquals(clientId, created.clientId());
        assertNotNull(created.createdAt());
    }

    @Test
    public void shouldCreateMultipleClientRewards() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        clientRewardRepository.createClientReward(id1, "user1@example.com");
        clientRewardRepository.createClientReward(id2, "user2@example.com");

        var orders = clientRewardRepository.getAllClientRewards();
        assertTrue(orders.size() >= 2);
    }

    @Test
    public void shouldGetAllClientRewards() {
        var clientId = UUID.randomUUID();
        clientRewardRepository.createClientReward(clientId, "getall@example.com");

        var orders = clientRewardRepository.getAllClientRewards();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void shouldAddBenefitsToNewClient() {
        var clientId = UUID.randomUUID();
        clientRewardRepository.createClientReward(clientId, "benefits@example.com");

        clientRewardRepository.addBenefitsToClientById(clientId, 100, new BigDecimal("25.50"));

        var reward = clientRewardRepository.getRewardByClientId(clientId).orElseThrow();
        assertEquals(100L, reward.points());
    }

    @Test
    public void shouldAccumulatePointsOnDuplicateKey() {
        var clientId = UUID.randomUUID();
        clientRewardRepository.createClientReward(clientId, "accumulate@example.com");

        clientRewardRepository.addBenefitsToClientById(clientId, 50, new BigDecimal("10.00"));
        clientRewardRepository.addBenefitsToClientById(clientId, 30, new BigDecimal("5.00"));

        var reward = clientRewardRepository.getRewardByClientId(clientId).orElseThrow();
        assertEquals(80L, reward.points());
    }

    @Test
    public void shouldUpdateRewardEmail() {
        var clientId = UUID.randomUUID();
        clientRewardRepository.createClientReward(clientId, "old@example.com");

        var updated = clientRewardRepository.updateRewardEmailById(clientId, "new@example.com");

        assertTrue(updated.isPresent());
        assertEquals("new@example.com", updated.get().email());
        assertEquals(clientId, updated.get().clientId());
    }

    @Test
    public void shouldReturnEmptyWhenUpdatingNonExistentEmail() {
        var result = clientRewardRepository.updateRewardEmailById(UUID.randomUUID(), "nobody@example.com");

        assertTrue(result.isEmpty());
    }
}
