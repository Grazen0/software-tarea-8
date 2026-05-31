package xyz.grazen.restaurant.infrastructure.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;
import xyz.grazen.restaurant.infrastructure.dto.OrderNotificationDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitOrderNotifierTest {

    @Mock
    private RabbitTemplate template;

    @Mock
    private Queue queue;

    @InjectMocks
    private RabbitOrderNotifier notifier;

    @Test
    void shouldSendOrderNotification() {
        when(queue.getName()).thenReturn("orders");

        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE", BigDecimal.TEN, null);
        notifier.notifyOrder(order, "card-456");

        var captor = ArgumentCaptor.forClass(OrderNotificationDto.class);
        verify(template).convertAndSend(eq("orders"), captor.capture());

        var dto = captor.getValue();
        assertEquals(order.clientId(), dto.clientId());
        assertEquals(order.total(), dto.total());
        assertEquals("card-456", dto.cardId());
    }

    @Test
    void shouldUseCorrectQueueName() {
        when(queue.getName()).thenReturn("my-custom-queue");

        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE", BigDecimal.TEN, null);
        notifier.notifyOrder(order, "card-123");

        var captor = ArgumentCaptor.forClass(OrderNotificationDto.class);
        verify(template).convertAndSend(eq("my-custom-queue"), captor.capture());
    }

    @Test
    void shouldSendMultipleNotifications() {
        when(queue.getName()).thenReturn("orders");

        var order1 = new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE-1", BigDecimal.TEN, null);
        var order2 = new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE-2", BigDecimal.valueOf(25.50), null);

        notifier.notifyOrder(order1, "card-aaa");
        notifier.notifyOrder(order2, "card-bbb");

        var captor = ArgumentCaptor.forClass(OrderNotificationDto.class);
        verify(template, times(2)).convertAndSend(eq("orders"), captor.capture());

        var allDtos = captor.getAllValues();
        assertEquals(2, allDtos.size());
        assertEquals("card-aaa", allDtos.get(0).cardId());
        assertEquals("card-bbb", allDtos.get(1).cardId());
        assertEquals(order1.clientId(), allDtos.get(0).clientId());
        assertEquals(order2.clientId(), allDtos.get(1).clientId());
    }

    @Test
    void shouldIncludeRestaurantCodeInNotification() {
        when(queue.getName()).thenReturn("orders");

        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), "MY-RESTO", BigDecimal.TEN, null);
        notifier.notifyOrder(order, "card-xyz");

        var captor = ArgumentCaptor.forClass(OrderNotificationDto.class);
        verify(template).convertAndSend(eq("orders"), captor.capture());

        assertEquals("MY-RESTO", captor.getValue().restaurantCode());
    }
}
