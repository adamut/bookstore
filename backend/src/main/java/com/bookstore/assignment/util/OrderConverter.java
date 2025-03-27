package com.bookstore.assignment.util;

import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.models.OrderItem;
import com.bookstore.assignment.request.OrderItemRequest;
import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;

import java.util.List;

public class OrderConverter {

    public static OrderResponse entityToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customer(CustomerConverter.entityToResponse(order.getCustomer()))
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                //todo add here
//                .items()
                .build();
    }

    public static Order requestToEntity(OrderRequest customerRequest) {
        Order order = new Order();
        order.setId(customerRequest.getId());

        order.setCustomer(CustomerConverter.requestToEntity(customerRequest.getCustomer()));
        List<OrderItem> orderItemList = getOrderItems(customerRequest.getItems());

        order.setItems(orderItemList);
        order.setTotalPrice(customerRequest.getTotalPrice());
        order.setOrderDate(customerRequest.getOrderDate());

        return order;
    }

    private static List<OrderItem> getOrderItems(List<OrderItemRequest> orderItemRequests) {
        return orderItemRequests.stream()
                .map(OrderItemConverter::requestToEntity)
                .toList();
    }
}
