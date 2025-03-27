package com.bookstore.assignment.util;

import com.bookstore.assignment.models.Customer;
import com.bookstore.assignment.request.CustomerRequest;
import com.bookstore.assignment.response.CustomerResponse;

import static com.bookstore.assignment.util.ConverterUtil.doIfNotNull;

public class CustomerConverter {

    public static CustomerResponse entityToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .name(customer.getName())
                .build();
    }

    public static Customer requestToEntity(CustomerRequest customerRequest){
        Customer customer = new Customer();

        doIfNotNull(customerRequest.getId(), customer::setId);
        doIfNotNull(customerRequest.getName(), customer::setName);
        doIfNotNull(customerRequest.getLoyaltyPoints(), customer::setLoyaltyPoints);

        return customer;
    }

    public static CustomerRequest responseToRequest(CustomerResponse customer) {
        return CustomerRequest.builder()
                .id(customer.getId())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .name(customer.getName())
                .build();
    }
}
