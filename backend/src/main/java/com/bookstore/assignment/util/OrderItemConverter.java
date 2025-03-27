package com.bookstore.assignment.util;

import com.bookstore.assignment.models.OrderItem;
import com.bookstore.assignment.request.OrderItemRequest;
import com.bookstore.assignment.response.OrderItemResponse;

import java.util.List;

public class OrderItemConverter {


    public static OrderItemResponse entityToResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
//                    .id(orderItem.getId())
//                    .loyaltyPoints(orderItem.getLoyaltyPoints())
//                    .name(orderItem.getName())
                .build();
    }

    public static OrderItem requestToEntity(OrderItemRequest orderItemRequest) {
        OrderItem orderItem = new OrderItem();
//            orderItem.setId(orderItemRequest.getId());
//            orderItem.setName(orderItemRequest.getName());
//            orderItem.setLoyaltyPoints(orderItemRequest.getLoyaltyPoints());

        return orderItem;
    }

    public static List<OrderItem> requestToEntityList(List<OrderItemRequest> orderItemRequestList){
        return orderItemRequestList.stream()
                .map(OrderItemConverter::requestToEntity)
                .toList();
    }
}
