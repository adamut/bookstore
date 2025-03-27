package com.bookstore.assignment.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private int loyaltyPoints;
}
