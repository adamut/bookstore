package com.bookstore.backend.service;

import com.bookstore.assignment.dao.OrderItemRepository;
import com.bookstore.assignment.model.BookType;
import com.bookstore.assignment.models.Book;
import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.models.OrderItem;
import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.request.OrderItemRequest;
import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.BookResponse;
import com.bookstore.assignment.response.OrderItemResponse;
import com.bookstore.assignment.response.OrderResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.service.OrderItemServiceImpl;
import com.bookstore.assignment.util.BookConverter;
import com.bookstore.assignment.util.OrderConverter;
import com.bookstore.assignment.util.PageConverter;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PageConverter<OrderItem, OrderItemResponse> pageConverter;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private OrderItem orderItem;
    private OrderItemRequest orderItemRequest;
    private OrderItemResponse orderItemResponse;

    @BeforeEach
    void setUp() {

        Order order = getOrder();
        Book book = getBook();
        BookRequest bookRequest = getBookRequest();
        OrderRequest orderRequest = getOrderRequest();

        OrderResponse orderResponse = OrderConverter.entityToResponse(order);
        BookResponse bookResponse = BookConverter.entityToResponse(book);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(40));

        orderItemRequest = new OrderItemRequest();
        orderItemRequest.setId(1L);
        orderItemRequest.setOrder(orderRequest);
        orderItemRequest.setBook(bookRequest);
        orderItemRequest.setQuantity(2);
        orderItemRequest.setPrice(BigDecimal.valueOf(40));

        orderItemResponse = new OrderItemResponse();
        orderItemResponse.setId(1L);
        orderItemResponse.setBook(bookResponse);
        orderItemResponse.setQuantity(2);
        orderItemResponse.setPrice(BigDecimal.valueOf(40));
    }


    @Test
    void createOrderItem_Success() {
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        Try<OrderItemResponse> result = orderItemService.createOrderItem(orderItemRequest);

        assertTrue(result.isSuccess());
        OrderItemResponse actualResult = result.get();

        assertEquals(orderItemResponse.getQuantity(), actualResult.getQuantity());
        assertEquals(orderItemResponse.getPrice(), actualResult.getPrice());
        assertEquals(orderItemResponse.getBook().getId(), actualResult.getBook().getId());
        assertEquals(orderItemResponse.getBook().getType(), actualResult.getBook().getType());

        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void createOrderItem_Failure() {
        when(orderItemRepository.save(any(OrderItem.class))).thenThrow(new RuntimeException("Database error"));

        Try<OrderItemResponse> result = orderItemService.createOrderItem(orderItemRequest);

        assertTrue(result.isFailure());
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void getOrderItemById_Success() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        Try<OrderItemResponse> result = orderItemService.getOrderItemById(1L);

        assertTrue(result.isSuccess());
        assertEquals(orderItemResponse, result.get());
    }

    @Test
    void getOrderItemById_NotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        Try<OrderItemResponse> result = orderItemService.getOrderItemById(1L);

        assertTrue(result.isFailure());
    }

    @Test
    void getAllOrderItems_Success() {
        Page<OrderItem> orderItemPage = new PageImpl<>(List.of(orderItem));
        Pageable pageable = PageRequest.of(0, 10);
        PageResponse<OrderItemResponse> pageResponse = new PageResponse<>();

        when(orderItemRepository.findAll(pageable)).thenReturn(orderItemPage);
        when(pageConverter.toPageResponse(any(), any())).thenReturn(pageResponse);

        Try<PageResponse<OrderItemResponse>> result = orderItemService.getAllOrderItems(0, 10);

        assertTrue(result.isSuccess());
        assertEquals(pageResponse, result.get());
    }

    @Test
    void updateOrderItem_Success() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        Try<OrderItemResponse> result = orderItemService.updateOrderItem(orderItemRequest);

        assertTrue(result.isSuccess());
        assertEquals(orderItemResponse, result.get());
    }

    @Test
    void updateOrderItem_NotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        Try<OrderItemResponse> result = orderItemService.updateOrderItem(orderItemRequest);

        assertTrue(result.isFailure());
    }

    @Test
    void deleteOrderItem_Success() {
        doNothing().when(orderItemRepository).deleteById(1L);

        Try<Void> result = orderItemService.deleteOrderItem(1L);

        assertTrue(result.isSuccess());
        verify(orderItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrderItem_Failure() {
        doThrow(new RuntimeException("Deletion error")).when(orderItemRepository).deleteById(1L);

        Try<Void> result = orderItemService.deleteOrderItem(1L);

        assertTrue(result.isFailure());
    }

    private static Book getBook() {
        Book book = new Book();
        book.setId(5L);
        book.setTitle("Test Book");
        book.setStock(50);
        book.setPrice(BigDecimal.valueOf(20));
        book.setType(BookType.REGULAR);
        return book;
    }

    private static Order getOrder() {
        Order order = new Order();
        order.setId(10L);
        order.setTotalPrice(BigDecimal.valueOf(100));
        return order;
    }

    private static OrderRequest getOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId(10L);
        orderRequest.setTotalPrice(BigDecimal.valueOf(100));
        return orderRequest;
    }

    private static BookRequest getBookRequest() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setId(5L);
        bookRequest.setTitle("Test Book");
        bookRequest.setStock(50);
        bookRequest.setPrice(BigDecimal.valueOf(20));
        bookRequest.setType(BookType.REGULAR);

        return bookRequest;
    }
}
