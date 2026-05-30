package xyz.grazen.restaurant.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.restaurant.domain.model.Order;
import xyz.grazen.restaurant.domain.ports.OrderNotifier;
import xyz.grazen.restaurant.domain.ports.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderNotifier messageService;

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order placeOrder(UUID clientId, String restaurantCode, BigDecimal total, String cardId) {
        Order order = orderRepository.createOrder(clientId, restaurantCode, total);
        messageService.notifyOrder(order, cardId);
        return order;
    }

}
