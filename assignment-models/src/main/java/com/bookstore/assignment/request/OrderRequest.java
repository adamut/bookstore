package com.bookstore.assignment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private Long id;

    private CustomerRequest customer;

    private List<OrderItemRequest> items;

    private BigDecimal totalPrice;

    private Instant orderDate;
}
