package xyz.grazen.restaurant.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import xyz.grazen.restaurant.domain.model.Order;

public interface OrderService {

    public List<Order> getAllOrders();

    public Order placeOrder(UUID clientId, String restaurantCode, BigDecimal total, String cardId);

}
