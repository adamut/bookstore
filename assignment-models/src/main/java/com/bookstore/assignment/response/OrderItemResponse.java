package com.bookstore.assignment.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private Long id;
    private OrderResponse order;
    private BookResponse book;
    private int quantity;
    private BigDecimal price;

}
