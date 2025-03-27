package com.bookstore.assignment;

import com.bookstore.assignment.response.CustomerLoyaltyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("bookstore/customer")
public interface CustomerResource {

    @GetMapping("/loyalty/{id}")
    ResponseEntity<CustomerLoyaltyResponse> getLoyaltyPoints(@PathVariable("id") Long customerId);
}
