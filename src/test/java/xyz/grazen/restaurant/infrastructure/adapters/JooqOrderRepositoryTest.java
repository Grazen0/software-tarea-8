package xyz.grazen.restaurant.infrastructure.adapters;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.grazen.restaurant.TestcontainersConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
@Testcontainers
public class JooqOrderRepositoryTest {

    @Autowired
    JooqOrderRepository orderRepository;

    @Test
    public void testThing() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        orderRepository.createOrder(id1, "123", new BigDecimal(2.0));
        orderRepository.createOrder(id2, "456", new BigDecimal(3.14));

        var orders = orderRepository.getAllOrders();
        assertEquals(2, orders.size());
    }
}
