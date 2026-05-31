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
    public void shouldReturnEmptyListInitially() {
        var orders = orderRepository.getAllOrders();
        assertNotNull(orders);
        assertEquals(0, orders.size());
    }

    @Test
    public void shouldCreateOrderWithAllFields() {
        var clientId = UUID.randomUUID();
        var created = orderRepository.createOrder(clientId, "CODE-1", new BigDecimal("99.99"));

        assertNotNull(created);
        assertEquals(clientId, created.clientId());
        assertEquals("CODE-1", created.restaurantCode());
        assertEquals(new BigDecimal("99.99"), created.total());
        assertNotNull(created.id());
        assertNotNull(created.createdAt());
    }

    @Test
    public void shouldCreateMultipleOrders() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        orderRepository.createOrder(id1, "RESTO-1", new BigDecimal("10.00"));
        orderRepository.createOrder(id2, "RESTO-2", new BigDecimal("25.50"));

        var orders = orderRepository.getAllOrders();
        assertTrue(orders.size() >= 2);
    }

    @Test
    public void shouldGetAllOrders() {
        var clientId = UUID.randomUUID();
        orderRepository.createOrder(clientId, "ALL-TEST", new BigDecimal("5.00"));

        var orders = orderRepository.getAllOrders();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void shouldHandleDifferentDecimalValues() {
        var small = new BigDecimal("0.01");
        var large = new BigDecimal("9999.99");

        orderRepository.createOrder(UUID.randomUUID(), "SMALL", small);
        orderRepository.createOrder(UUID.randomUUID(), "LARGE", large);

        var orders = orderRepository.getAllOrders();
        assertTrue(orders.size() >= 2);
    }
}
