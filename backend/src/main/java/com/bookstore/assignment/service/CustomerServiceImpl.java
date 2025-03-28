package com.bookstore.assignment.service;

import com.bookstore.assignment.dao.CustomerRepository;
import com.bookstore.assignment.models.Customer;
import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.request.CustomerRequest;
import com.bookstore.assignment.response.CustomerResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.util.CustomerConverter;
import com.bookstore.assignment.util.PageConverter;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.bookstore.assignment.util.ConverterUtil.doIfNotNull;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PageConverter<Customer, CustomerResponse> pageConverter;

    public CustomerServiceImpl(CustomerRepository customerRepository, PageConverter<Customer, CustomerResponse> pageConverter) {
        this.customerRepository = customerRepository;
        this.pageConverter = pageConverter;
    }

    @Override
    public Try<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        return Try.of(() -> CustomerConverter.requestToEntity(customerRequest))
                .map(customerRepository::save)
                .map(CustomerConverter::entityToResponse)
                .onSuccess(result -> log.debug("Successfully saved customer={}", result))
                .onFailure(exception -> log.error("Failed to save customer for request={}", customerRequest, exception));
    }

    @Override
    public Try<CustomerResponse> getCustomerById(Long id) {
        return Try.of(() -> customerRepository.findById(id))
                .map(customerOptional -> customerOptional.map(CustomerConverter::entityToResponse)
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found")))
                .onFailure(error -> log.error("Unable to retrieve customer for id={}", id, error));
    }

    @Override
    public Try<PageResponse<CustomerResponse>> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return Try.of(() -> customerRepository.findAll(pageable))
                .map(customerPage -> pageConverter.toPageResponse(customerPage, CustomerConverter::entityToResponse))
                .onFailure(exception -> log.error("Unable to retrieve customer list for page={} and size={}", page, size, exception))
                .onSuccess(result -> log.debug("Successfully retrieved customers for page={} and size={} ", page, size));
    }

    @Override
    public Try<CustomerResponse> updateCustomer(CustomerRequest updatedCustomer) {
        var customerId = updatedCustomer.getId();

        return Try.of(() -> customerRepository.findById(customerId))
                .map(customerOptional -> customerOptional
                        .map(customer -> updateCustomer(customer, updatedCustomer))
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found")))
                .map(CustomerConverter::entityToResponse)
                .onSuccess(customerResponse -> log.debug("Customer with id={} was successfully updated", customerId))
                .onFailure(exception -> log.error("Unable to update customer with id={}", customerId, exception));
    }

    private Customer updateCustomer(Customer existingCustomer, CustomerRequest updatedCustomer) {
        doIfNotNull(updatedCustomer.getLoyaltyPoints(), existingCustomer::setLoyaltyPoints);
        doIfNotNull(updatedCustomer.getName(), existingCustomer::setName);

        return customerRepository.save(existingCustomer);
    }

    @Override
    public Try<Void> deleteCustomer(Long id) {
        return Try.run(() -> customerRepository.deleteById(id))
                .onSuccess(unused -> log.debug("Successfully deleted customer for id={}", id))
                .onFailure(exception -> log.error("Failed to delete customer for id={}", id));
    }
}
