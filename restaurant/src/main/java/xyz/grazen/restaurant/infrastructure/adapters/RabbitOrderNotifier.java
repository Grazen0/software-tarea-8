package xyz.grazen.restaurant.infrastructure.adapters;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.restaurant.domain.model.Order;
import xyz.grazen.restaurant.domain.ports.OrderNotifier;
import xyz.grazen.restaurant.infrastructure.dto.OrderNotificationDto;

@Service
@RequiredArgsConstructor
public class RabbitOrderNotifier implements OrderNotifier {

    private final RabbitTemplate template;
    private final Queue queue;

    public void notifyOrder(Order order, String cardId) {
        var dto = new OrderNotificationDto(order, cardId);

        var messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT);
        template.convertAndSend(queue.getName(), dto);
    }

}
