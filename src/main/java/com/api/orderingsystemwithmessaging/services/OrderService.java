package com.api.orderingsystemwithmessaging.services;

import com.api.orderingsystemwithmessaging.dtos.OrderDto;
import com.api.orderingsystemwithmessaging.models.OrderModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    public OrderModel saveOrder(OrderDto orderDto);
    public Optional<OrderModel> findOrder(UUID id);
    public void endOrder(OrderModel orderModel);
    public List<OrderModel> getAllOrdersInProgress();
}
