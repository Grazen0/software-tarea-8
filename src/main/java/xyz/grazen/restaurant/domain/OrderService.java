package xyz.grazen.restaurant.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.restaurant.domain.model.Order;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order createOrder(UUID clientId, String restaurantCode) {
        return orderRepository.createOrder(clientId, restaurantCode);
    }
}
