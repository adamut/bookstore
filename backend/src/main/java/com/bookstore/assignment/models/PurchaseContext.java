package com.bookstore.assignment.models;

import com.bookstore.assignment.request.PurchaseRequest;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Book, Integer> eligibleForDiscount = new HashMap<>();

    @Builder.Default
    private List<Book> booksToUpdate = new ArrayList<>();

    @Builder.Default
    private List<Book> orderBooks = new ArrayList<>();

    public static PurchaseContext of(PurchaseRequest request, Customer customer) {
        return PurchaseContext.builder()
                .request(request)
                .customer(customer)
                .build();
    }
}
