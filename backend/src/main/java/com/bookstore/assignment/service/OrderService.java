package com.bookstore.assignment.service;

import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;
import io.vavr.control.Try;

public interface OrderService {
    Try<OrderResponse> createOrder(OrderRequest orderRequest);

    Try<OrderResponse> updateOrder(OrderRequest order);
}
