package xyz.grazen.rewards.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import xyz.grazen.rewards.application.dto.OrderNotificationDto;
import xyz.grazen.rewards.domain.ClientRewardService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderReceiverTest {

    @Mock
    private ClientRewardService clientRewardService;

    @InjectMocks
    private OrderReceiver orderReceiver;

    @Test
    void shouldCallEmitRewardWithCorrectArguments() {
        var clientId = UUID.randomUUID();
        var createdAt = OffsetDateTime.now();
        var notif = new OrderNotificationDto(clientId, new BigDecimal("75.00"), "card-123", "RESTO-1", createdAt);

        orderReceiver.receiveOrder(notif);

        verify(clientRewardService).emitReward(clientId, new BigDecimal("75.00"), "card-123", "RESTO-1", createdAt);
    }

    @Test
    void shouldHandleZeroTotal() {
        var notif = new OrderNotificationDto(UUID.randomUUID(), BigDecimal.ZERO, "card-abc", "RESTO-2", OffsetDateTime.now());

        orderReceiver.receiveOrder(notif);

        verify(clientRewardService).emitReward(any(), eq(BigDecimal.ZERO), any(), any(), any());
    }

    @Test
    void shouldPropagateServiceException() {
        var notif = new OrderNotificationDto(UUID.randomUUID(), BigDecimal.TEN, "card-x", "RESTO-3", OffsetDateTime.now());
        doThrow(new RuntimeException("DB error"))
                .when(clientRewardService).emitReward(any(), any(), any(), any(), any());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> orderReceiver.receiveOrder(notif));
    }
}
