package com.bookstore.assignment.controller.admin;

import com.bookstore.assignment.admin.OrderAdminResource;
import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;
import com.bookstore.assignment.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAdminResourceImpl implements OrderAdminResource {

    private final OrderService orderService;

    public OrderAdminResourceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest)
                .map(ResponseEntity::ok)
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }

    @Override
    public ResponseEntity<OrderResponse> updateCustomer(OrderRequest orderRequest) {
        return orderService.updateOrder(orderRequest)
                .map(ResponseEntity::ok)
                .recover(CustomerNotFoundException.class, cause -> ResponseEntity.notFound().build())
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }
}
