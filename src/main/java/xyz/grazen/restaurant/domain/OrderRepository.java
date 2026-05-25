package xyz.grazen.restaurant.domain;

import java.util.List;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;

public interface OrderRepository {

    public List<Order> getAllOrders();

    public Order createOrder(UUID clientId, String restaurantCode);

}
