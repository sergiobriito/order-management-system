package com.api.orderingsystemwithmessaging.services;

import com.api.orderingsystemwithmessaging.configs.rabbitmq.RabbitMQService;
import com.api.orderingsystemwithmessaging.dtos.OrderDto;
import com.api.orderingsystemwithmessaging.models.OrderModel;
import com.api.orderingsystemwithmessaging.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final RabbitMQService rabbitMQService;

    public OrderServiceImpl(OrderRepository orderRepository, RabbitMQService rabbitMQService) {
        this.orderRepository = orderRepository;
        this.rabbitMQService = rabbitMQService;
    }

    @Transactional
    public OrderModel saveOrder(OrderDto orderDto){
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDto, orderModel);
        orderModel.setOrderDate(LocalDateTime.now(ZoneId.of("UTC")));
        orderRepository.save(orderModel);
        return orderModel;
    };

    @Transactional
    public Optional<OrderModel> findOrder(UUID id){
        return orderRepository.findById(id);
    };

    public void endOrder(OrderModel orderModel){
        String messageMQP = "{ " +
                "\"OrderId\": \"" + orderModel.getId() +
                "\", \"Owner\": \"" + orderModel.getOwner() +
                "\", \"OrderDate\": \"" + orderModel.getOrderDate() +
                "\", \"Product\": \"" + orderModel.getProduct() +
                "\", \"Description\": \"" + orderModel.getDescription() +
                "\" }";
        orderRepository.endOrderById(orderModel.getId());
        rabbitMQService.sendMessage(messageMQP);
    }


    public List<OrderModel> getAllOrdersInProgress(){
        return orderRepository.getAllOrdersInProgress();
    };
}
