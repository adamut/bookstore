package com.bookstore.assignment.controller;

import com.bookstore.assignment.OrderResource;
import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.response.OrderResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResourceImpl implements OrderResource {

    private final OrderService orderService;

    public OrderResourceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public PageResponse<OrderResponse> getOrders(Integer page, Integer size) {
        return orderService.getAllOrders(page, size)
                .get();
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .recover(BookNotFoundException.class, cause -> ResponseEntity.notFound().build())
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }
}
