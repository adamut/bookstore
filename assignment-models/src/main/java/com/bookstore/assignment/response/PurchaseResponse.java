package com.bookstore.assignment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {

    private Long orderId;
    private BigDecimal totalPrice;
    private int earnedLoyaltyPoints;
    private int remainingLoyaltyPoints;
}
