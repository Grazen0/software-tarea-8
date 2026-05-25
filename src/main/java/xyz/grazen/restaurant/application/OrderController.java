package xyz.grazen.restaurant.application;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import xyz.grazen.restaurant.application.dto.CreateOrderDto;
import xyz.grazen.restaurant.application.dto.OrderResponseDto;
import xyz.grazen.restaurant.domain.OrderService;
import xyz.grazen.restaurant.domain.model.Order;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        var orders = orderService.getAllOrders();
        return orders.stream().map(OrderResponseDto::new).toList();
    }

    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody CreateOrderDto dto) {
        Order order = orderService.createOrder(dto.clientId(), dto.restaurantCode());
        return new OrderResponseDto(order);
    }
}
