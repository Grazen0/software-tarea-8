package xyz.grazen.restaurant.infrastructure;

import java.util.List;

import org.jooq.Asterisk;
import org.jooq.DSLContext;

import lombok.RequiredArgsConstructor;
import xyz.grazen.restaurant.domain.Order;
import static org.jooq.impl.DSL.*;

@RequiredArgsConstructor
public class OrderRepository {

    private final DSLContext dsl;

    public List<Order> getAllOrders() {
        return dsl.select(asterisk()).from(orders);
    }
}
