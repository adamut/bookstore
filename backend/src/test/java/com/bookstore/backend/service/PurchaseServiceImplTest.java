package com.bookstore.backend.service;

import com.bookstore.assignment.config.LoyaltyPointsConfig;
import com.bookstore.assignment.dao.BookRepository;
import com.bookstore.assignment.dao.CustomerRepository;
import com.bookstore.assignment.dao.OrderItemRepository;
import com.bookstore.assignment.dao.OrderRepository;
import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.model.BookType;
import com.bookstore.assignment.models.Book;
import com.bookstore.assignment.models.Customer;
import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.request.BookOrderItem;
import com.bookstore.assignment.request.PurchaseRequest;
import com.bookstore.assignment.response.PurchaseResponse;
import com.bookstore.assignment.service.PurchaseServiceImpl;
import com.bookstore.assignment.util.DiscountCalculator;
import io.vavr.control.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private DiscountCalculator discountCalculator;

    @Mock
    private LoyaltyPointsConfig loyaltyPointsConfig;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private Customer testCustomer;
    private Book testBook;
    private PurchaseRequest testRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(1L, "John Doe", 5);
        testBook = new Book(1L, "Test Book", "A great book", BigDecimal.valueOf(50.00), BookType.REGULAR, 10, Instant.now(), Instant.now());

        List<BookOrderItem> bookOrderItems = List.of(new BookOrderItem(1L, 2));
        testRequest = new PurchaseRequest(1L, bookOrderItems, true);
    }

    @Test
    void purchaseBooks_SuccessfulPurchase() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(discountCalculator.applyDiscount(any(), any())).thenReturn(BigDecimal.valueOf(50));
        when(loyaltyPointsConfig.getPointsThreshold()).thenReturn(10);

        // Act
        Try<PurchaseResponse> result = purchaseService.purchaseBooks(testRequest);

        // Assert
        Assertions.assertTrue(result.isSuccess());
        PurchaseResponse purchaseResponse = result.get();
        Assertions.assertEquals(Long.valueOf(1L), purchaseResponse.getOrderId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void purchaseBooks_CustomerNotFound_ShouldFail() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Try<PurchaseResponse> result = purchaseService.purchaseBooks(testRequest);

        // Assert
        Assertions.assertTrue(result.isFailure());
        assertInstanceOf(CustomerNotFoundException.class, result.getCause());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void purchaseBooks_BookNotFound_ShouldFail() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Try<PurchaseResponse> result = purchaseService.purchaseBooks(testRequest);

        // Assert
        Assertions.assertTrue(result.isFailure());
        assertInstanceOf(BookNotFoundException.class, result.getCause());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void purchaseBooks_NotEnoughStock_ShouldFail() {
        // Arrange
        testBook.setStock(1); // Less stock than requested
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        Try<PurchaseResponse> result = purchaseService.purchaseBooks(testRequest);

        // Assert
        Assertions.assertTrue(result.isFailure());
        assertInstanceOf(RuntimeException.class, result.getCause());
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void purchaseBooks_ApplyLoyaltyPoints_ShouldReduceTotalPrice() {
        // Arrange
        testCustomer.setLoyaltyPoints(10);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any())).thenReturn(testCustomer);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(discountCalculator.applyDiscount(any(), any())).thenReturn(BigDecimal.valueOf(50));
        when(loyaltyPointsConfig.getPointsThreshold()).thenReturn(10);

        // Act
        Try<PurchaseResponse> result = purchaseService.purchaseBooks(testRequest);

        // Assert
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(BigDecimal.valueOf(50.0), result.get().getTotalPrice()); // Since loyalty points cover one book
        Assertions.assertEquals(2, result.get().getRemainingLoyaltyPoints());
    }

    @Test
    void purchaseBooks_FailureToSaveOrder_ShouldFail() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        Try<PurchaseResponse> result = purchaseService.purchaseBooks(testRequest);

        // Assert
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals("Database error", result.getCause().getMessage());
    }
}