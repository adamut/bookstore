package com.bookstore.assignment.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    private Long id;
    private OrderRequest order;
    private BookRequest book;
    private int quantity;
    private BigDecimal price;

}
