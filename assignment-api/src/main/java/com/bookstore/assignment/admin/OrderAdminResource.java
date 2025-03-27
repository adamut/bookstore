package com.bookstore.assignment.admin;

import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("bookstore/order")
public interface OrderAdminResource {

    @PostMapping
    ResponseEntity<OrderResponse> createOrder(@RequestBody @NotNull OrderRequest orderRequest);

    @PutMapping
    ResponseEntity<OrderResponse> updateCustomer(@RequestBody @NotNull OrderRequest orderRequest);
}
