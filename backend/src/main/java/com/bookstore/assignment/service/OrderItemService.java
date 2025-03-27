package com.bookstore.assignment.service;

import com.bookstore.assignment.request.OrderItemRequest;
import com.bookstore.assignment.response.OrderItemResponse;
import com.bookstore.assignment.response.PageResponse;
import io.vavr.control.Try;

public interface OrderItemService {
    Try<OrderItemResponse> createOrderItem(OrderItemRequest orderItemRequest);

    Try<OrderItemResponse> getOrderItemById(Long id);

    Try<PageResponse<OrderItemResponse>> getAllOrderItems(int page, int size);

    Try<OrderItemResponse> updateOrderItem(OrderItemRequest updatedOrderItem);

    Try<Void> deleteOrderItem(Long id);
}
