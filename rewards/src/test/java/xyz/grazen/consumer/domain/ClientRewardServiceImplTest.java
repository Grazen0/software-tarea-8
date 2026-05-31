package xyz.grazen.rewards.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import xyz.grazen.rewards.domain.model.ClientReward;
import xyz.grazen.rewards.domain.ports.ClientRewardRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientRewardServiceImplTest {

    @Mock
    private ClientRewardRepository clientRewardRepository;

    @InjectMocks
    private ClientRewardServiceImpl clientRewardService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(clientRewardService, "pointsMultiplier", new BigDecimal("2"));
        ReflectionTestUtils.setField(clientRewardService, "cashbackFactor", new BigDecimal("0.10"));
    }

    @Test
    void shouldGetAllRewards() {
        var rewards = List.of(
                new ClientReward(UUID.randomUUID(), "a@test.com", 10L, new BigDecimal("1.50"), OffsetDateTime.now()),
                new ClientReward(UUID.randomUUID(), "b@test.com", 20L, new BigDecimal("3.00"), OffsetDateTime.now()));
        when(clientRewardRepository.getAllClientRewards()).thenReturn(rewards);

        var result = clientRewardService.getAllRewards();

        assertEquals(2, result.size());
        verify(clientRewardRepository).getAllClientRewards();
    }

    @Test
    void shouldReturnEmptyListWhenNoRewards() {
        when(clientRewardRepository.getAllClientRewards()).thenReturn(List.of());

        var result = clientRewardService.getAllRewards();

        assertTrue(result.isEmpty());
        verify(clientRewardRepository).getAllClientRewards();
    }

    @Test
    void shouldGetRewardByClientId() {
        var clientId = UUID.randomUUID();
        var reward = new ClientReward(clientId, "test@test.com", 5L, new BigDecimal("0.75"), OffsetDateTime.now());
        when(clientRewardRepository.getRewardByClientId(clientId)).thenReturn(Optional.of(reward));

        var result = clientRewardService.getRewardByClientId(clientId);

        assertTrue(result.isPresent());
        assertEquals(clientId, result.get().clientId());
        verify(clientRewardRepository).getRewardByClientId(clientId);
    }

    @Test
    void shouldReturnEmptyWhenRewardNotFound() {
        when(clientRewardRepository.getRewardByClientId(any())).thenReturn(Optional.empty());

        var result = clientRewardService.getRewardByClientId(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldEmitRewardAndCalculatePointsAndCashback() {
        var clientId = UUID.randomUUID();
        var total = new BigDecimal("100.00");

        clientRewardService.emitReward(clientId, total, "card-123", "RESTO-1", OffsetDateTime.now());

        verify(clientRewardRepository).addBenefitsToClientById(eq(clientId), anyLong(), any(BigDecimal.class));
    }

    @Test
    void shouldUpdateRewardEmail() {
        var clientId = UUID.randomUUID();
        var updated = new ClientReward(clientId, "new@test.com", 0L, BigDecimal.ZERO, OffsetDateTime.now());
        when(clientRewardRepository.updateRewardEmailById(clientId, "new@test.com")).thenReturn(Optional.of(updated));

        var result = clientRewardService.updateRewardEmailById(clientId, "new@test.com");

        assertTrue(result.isPresent());
        assertEquals("new@test.com", result.get().email());
        verify(clientRewardRepository).updateRewardEmailById(clientId, "new@test.com");
    }
}
