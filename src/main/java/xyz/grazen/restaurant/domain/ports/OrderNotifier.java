package xyz.grazen.restaurant.domain.ports;

import xyz.grazen.restaurant.domain.model.Order;

public interface OrderNotifier {

    void notifyOrder(Order order, String cardId);

}
