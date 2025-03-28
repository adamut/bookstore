package com.bookstore.assignment.controller;

import com.bookstore.assignment.CustomerResource;
import com.bookstore.assignment.response.CustomerLoyaltyResponse;
import com.bookstore.assignment.response.CustomerResponse;
import com.bookstore.assignment.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CustomerResourceImpl implements CustomerResource {

    private final CustomerService customerService;

    public CustomerResourceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<CustomerLoyaltyResponse> getLoyaltyPoints(Long customerId) {
        return customerService.getCustomerById(customerId)
                .map(customer -> CustomerLoyaltyResponse.builder()
                        .id(customerId)
                        .loyaltyPoints(customer.getLoyaltyPoints())
                        .build())
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomer(Long customerId) {
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.notFound().build());
    }
}
