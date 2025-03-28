package com.bookstore.assignment.util;

import com.bookstore.assignment.models.OrderItem;
import com.bookstore.assignment.request.OrderItemRequest;
import com.bookstore.assignment.response.OrderItemResponse;

import java.util.List;

public class OrderItemConverter {

    private OrderItemConverter() {
    }

    public static OrderItemResponse entityToResponse(OrderItem orderItem) {
        OrderItemResponse.OrderItemResponseBuilder orderItemResponseBuilder = OrderItemResponse.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice());


        if (orderItem.getBook() != null) {
            orderItemResponseBuilder.book(BookConverter.entityToResponse(orderItem.getBook()));
        }
        return orderItemResponseBuilder.build();
    }

    public static OrderItem requestToEntity(OrderItemRequest orderItemRequest) {
        OrderItem orderItem = new OrderItem();

        orderItem.setId(orderItemRequest.getId());

        if (orderItemRequest.getOrder() != null) {
            orderItem.setOrder(OrderConverter.requestToEntity(orderItemRequest.getOrder()));
        }

        if (orderItemRequest.getBook() != null) {
            orderItem.setBook(BookConverter.requestToEntity(orderItemRequest.getBook()));
        }
        orderItem.setQuantity(orderItem.getQuantity());
        orderItem.setPrice(orderItem.getPrice());

        return orderItem;
    }

    public static List<OrderItem> requestToEntityList(List<OrderItemRequest> orderItemRequestList) {
        return orderItemRequestList.stream()
                .map(OrderItemConverter::requestToEntity)
                .toList();
    }

    public static List<OrderItemResponse> entityToResponseList(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(OrderItemConverter::entityToResponse)
                .toList();
    }
}
