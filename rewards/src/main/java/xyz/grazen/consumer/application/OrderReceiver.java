package xyz.grazen.rewards.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.grazen.rewards.application.dto.OrderNotificationDto;
import xyz.grazen.rewards.domain.ClientRewardService;

@Service
@RequiredArgsConstructor
public class OrderReceiver {

    private final ClientRewardService clientRewardService;

    Logger logger = LoggerFactory.getLogger(OrderReceiver.class);

    @RabbitListener(queues = "#{ordersQueue.name}")
    public void receiveOrder(OrderNotificationDto notif) {
        logger.info("Received order from client: " + notif.clientId());

        clientRewardService.emitReward(
                notif.clientId(),
                notif.total(),
                notif.cardId(),
                notif.restaurantCode(),
                notif.createdAt());
    }

}
