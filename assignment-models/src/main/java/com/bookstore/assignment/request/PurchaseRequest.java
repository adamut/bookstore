package com.bookstore.assignment.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {

    @NotNull
    private Long customerId;

    @NotNull
    @NotEmpty
    private List<BookOrderItem> books;

    private boolean useLoyaltyPoints;

}