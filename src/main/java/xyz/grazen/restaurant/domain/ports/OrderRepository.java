package xyz.grazen.restaurant.domain.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;

public interface OrderRepository {

    public List<Order> getAllOrders();

    public Order createOrder(UUID clientId, String restaurantCode, BigDecimal total);

}
