package com.bookstore.assignment.controller.admin;

import com.bookstore.assignment.admin.CustomerAdminResource;
import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.request.CustomerRequest;
import com.bookstore.assignment.response.CustomerResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerAdminResourceImpl implements CustomerAdminResource {

    private final CustomerService customerService;

    public CustomerAdminResourceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest)
                .map(ResponseEntity::ok)
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(CustomerRequest customerRequest) {
        return customerService.updateCustomer(customerRequest)
                .map(ResponseEntity::ok)
                .recover(CustomerNotFoundException.class, cause -> ResponseEntity.notFound().build())
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(Long id) {
        return customerService.deleteCustomer(id)
                .map(ResponseEntity::ok)
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }

    @Override
    public PageResponse<CustomerResponse> getCustomers(Integer page, Integer size) {
        return customerService.getAllCustomers(page, size)
                .get();
    }
}
