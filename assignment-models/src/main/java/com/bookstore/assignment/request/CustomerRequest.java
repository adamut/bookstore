package com.bookstore.assignment.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Builder.Default
    private int loyaltyPoints = 0;
}
