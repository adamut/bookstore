package com.bookstore.assignment.service;

import com.bookstore.assignment.request.CustomerRequest;
import com.bookstore.assignment.response.CustomerResponse;
import com.bookstore.assignment.response.PageResponse;
import io.vavr.control.Try;


public interface CustomerService {
    Try<CustomerResponse> createCustomer(CustomerRequest customer);

    Try<CustomerResponse> getCustomerById(Long id);

    Try<PageResponse<CustomerResponse>> getAllCustomers(int page, int size);

    Try<CustomerResponse> updateCustomer(CustomerRequest updatedCustomer);

    Try<Void> deleteCustomer(Long id);
}
