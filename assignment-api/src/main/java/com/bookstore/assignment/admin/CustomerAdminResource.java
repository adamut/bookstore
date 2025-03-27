package com.bookstore.assignment.admin;

import com.bookstore.assignment.request.CustomerRequest;
import com.bookstore.assignment.response.CustomerResponse;
import com.bookstore.assignment.response.PageResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("bookstore/admin/customer")
public interface CustomerAdminResource {

    @PostMapping
    ResponseEntity<CustomerResponse> createCustomer(@NotNull @RequestBody CustomerRequest customerRequest);

    @PutMapping
    ResponseEntity<CustomerResponse> updateCustomer(@NotNull @RequestBody CustomerRequest customerRequest);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCustomer(@NotNull @PathVariable("id") Long id);

    @GetMapping("/")
    PageResponse<CustomerResponse> getCustomers(@RequestParam(defaultValue = "0") @NotNull Integer page, @RequestParam(defaultValue = "10") @NotNull Integer size);
}
