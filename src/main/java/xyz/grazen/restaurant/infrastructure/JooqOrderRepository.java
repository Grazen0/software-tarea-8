package xyz.grazen.restaurant.infrastructure;

import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.restaurant.domain.OrderRepository;
import xyz.grazen.restaurant.domain.model.Order;
import xyz.grazen.restaurant.infrastructure.jooq.public_.tables.Orders;

import static org.jooq.impl.DSL.*;

@Service
@RequiredArgsConstructor
public class JooqOrderRepository implements OrderRepository {

    private final DSLContext dsl;
    private final Orders orders = Orders.ORDERS;

    public List<Order> getAllOrders() {
        return dsl.select(asterisk()).from(orders).fetchInto(Order.class);
    }

    public Order createOrder(UUID clientId, String restaurantCode) {
        return dsl.insertInto(orders)
                .set(orders.CLIENT_ID, clientId)
                .set(orders.RESTAURANT_CODE, restaurantCode)
                .returning()
                .fetchOneInto(Order.class);
    }
}
