package xyz.grazen.restaurant.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;
import xyz.grazen.restaurant.domain.ports.OrderNotifier;
import xyz.grazen.restaurant.domain.ports.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderNotifier orderNotifier;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldGetAllOrders() {
        var orders = List.of(
            new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE-1", BigDecimal.TEN, null),
            new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE-2", BigDecimal.ONE, null)
        );
        when(orderRepository.getAllOrders()).thenReturn(orders);

        var result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository).getAllOrders();
    }

    @Test
    void shouldReturnEmptyListWhenNoOrders() {
        when(orderRepository.getAllOrders()).thenReturn(List.of());

        var result = orderService.getAllOrders();

        assertTrue(result.isEmpty());
        verify(orderRepository).getAllOrders();
    }

    @Test
    void shouldPlaceOrderAndNotify() {
        var clientId = UUID.randomUUID();
        var order = new Order(UUID.randomUUID(), clientId, "CODE", BigDecimal.valueOf(50), null);
        when(orderRepository.createOrder(clientId, "CODE", BigDecimal.valueOf(50)))
            .thenReturn(order);

        var result = orderService.placeOrder(clientId, "CODE", BigDecimal.valueOf(50), "card-123");

        assertEquals(clientId, result.clientId());
        verify(orderRepository).createOrder(clientId, "CODE", BigDecimal.valueOf(50));
        verify(orderNotifier).notifyOrder(order, "card-123");
    }

    @Test
    void shouldReturnCreatedOrderFromPlaceOrder() {
        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE", BigDecimal.TEN, null);
        when(orderRepository.createOrder(any(), any(), any())).thenReturn(order);

        var result = orderService.placeOrder(order.clientId(), "CODE", BigDecimal.TEN, "card");

        assertEquals(order, result);
    }

    @Test
    void shouldPropagateRepositoryException() {
        when(orderRepository.createOrder(any(), any(), any()))
            .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
            () -> orderService.placeOrder(UUID.randomUUID(), "CODE", BigDecimal.TEN, "card"));
    }

    @Test
    void shouldPropagateNotifierException() {
        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), "CODE", BigDecimal.TEN, null);
        when(orderRepository.createOrder(any(), any(), any())).thenReturn(order);
        doThrow(new RuntimeException("RabbitMQ error"))
            .when(orderNotifier).notifyOrder(any(), any());

        assertThrows(RuntimeException.class,
            () -> orderService.placeOrder(order.clientId(), "CODE", BigDecimal.TEN, "card"));
    }
}
