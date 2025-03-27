package com.bookstore.assignment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private CustomerResponse customer;

    private List<OrderItemResponse> items;

    private BigDecimal totalPrice;

    private Instant orderDate;
}
