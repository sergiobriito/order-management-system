package com.api.orderingsystemwithmessaging.configs.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RabbitMQListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "queue")
    public void receiveMessageFromRabbitMQ(String message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
