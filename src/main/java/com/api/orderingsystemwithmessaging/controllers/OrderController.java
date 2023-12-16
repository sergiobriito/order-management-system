package com.api.orderingsystemwithmessaging.controllers;

import com.api.orderingsystemwithmessaging.dtos.OrderDto;
import com.api.orderingsystemwithmessaging.models.OrderModel;
import com.api.orderingsystemwithmessaging.services.OrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/order-system")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save-order")
    @Transactional
    public ResponseEntity<Object> saveOrder(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data.");
        };
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(orderDto));
    };

    @PatchMapping("/complete-order/{id}")
    @Transactional
    public ResponseEntity<Object> endOrder(@PathVariable("id") UUID id){
        Optional<OrderModel> order = orderService.findOrder(id);
        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }
        orderService.endOrder(order.get());
        return ResponseEntity.status(HttpStatus.OK).body("Order ended.");
    }

    @GetMapping("orders-in-progress")
    public ResponseEntity<Object> getAllOrdersInProgress(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersInProgress());
    };

}
