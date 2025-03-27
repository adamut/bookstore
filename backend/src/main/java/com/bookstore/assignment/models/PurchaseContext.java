package com.bookstore.assignment.models;

import com.bookstore.assignment.request.PurchaseRequest;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder()
public class PurchaseContext {

    private PurchaseRequest request;

    private Order order;

    private Customer customer;

    private List<OrderItem> orderItemList;

    private int earnedLoyaltyPoints;

    private BigDecimal totalPrice;

    @Builder.Default
    private List<Book> eligibleForDiscount = new ArrayList<>();

    @Builder.Default
    private List<Book> booksToUpdate = new ArrayList<>();

    @Builder.Default
    private List<Book> orderBooks = new ArrayList<>();
}
