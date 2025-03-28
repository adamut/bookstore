package com.bookstore.assignment.util;

import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;


import static com.bookstore.assignment.util.ConverterUtil.doIfNotNull;

public class OrderConverter {

    private OrderConverter() {
    }

    public static OrderResponse entityToResponse(Order order) {
        OrderResponse.OrderResponseBuilder orderResponseBuilder = OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate());

        doIfNotNull(order.getCustomer(), customer -> orderResponseBuilder.customer(CustomerConverter.entityToResponse(customer)));
        doIfNotNull(order.getItems(), items -> orderResponseBuilder.items(OrderItemConverter.entityToResponseList(items)));

        return orderResponseBuilder.build();
    }

    public static Order requestToEntity(OrderRequest orderRequest) {
        Order order = new Order();

        doIfNotNull(orderRequest.getId(), order::setId);
        doIfNotNull(orderRequest.getCustomer(), customerRequest -> order.setCustomer(CustomerConverter.requestToEntity(orderRequest.getCustomer())));
        doIfNotNull(orderRequest.getItems(), orderItemRequests -> order.setItems(OrderItemConverter.requestToEntityList(orderItemRequests)));
        doIfNotNull(orderRequest.getTotalPrice(), order::setTotalPrice);
        doIfNotNull(orderRequest.getOrderDate(), order::setOrderDate);

        return order;
    }
}
