package com.bookstore.assignment.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;

    private CustomerResponse customer;

    private List<OrderItemResponse> items;

    private BigDecimal totalPrice;

    private Instant orderDate;
}
