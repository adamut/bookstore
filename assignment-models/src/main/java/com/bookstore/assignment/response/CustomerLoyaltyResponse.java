package com.bookstore.assignment.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CustomerLoyaltyResponse {

    @NotNull
    private Long id;

    @NotNull
    private Integer loyaltyPoints;

}
