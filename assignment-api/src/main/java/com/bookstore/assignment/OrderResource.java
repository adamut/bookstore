package com.bookstore.assignment;

import com.bookstore.assignment.response.OrderResponse;
import com.bookstore.assignment.response.PageResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("bookstore/order")
public interface OrderResource {

    @GetMapping("/")
    PageResponse<OrderResponse> getOrders(@RequestParam(defaultValue = "0") @NotNull Integer page, @RequestParam(defaultValue = "10") @NotNull Integer size);

    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") @NotNull Long id);
}
