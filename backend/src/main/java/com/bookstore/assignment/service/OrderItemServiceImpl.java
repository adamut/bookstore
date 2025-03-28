package com.bookstore.assignment.service;

import com.bookstore.assignment.dao.OrderItemRepository;
import com.bookstore.assignment.exception.OrderItemNotFoundException;
import com.bookstore.assignment.models.OrderItem;
import com.bookstore.assignment.request.OrderItemRequest;
import com.bookstore.assignment.response.OrderItemResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.util.OrderItemConverter;
import com.bookstore.assignment.util.PageConverter;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.bookstore.assignment.util.ConverterUtil.doIfNotNull;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final PageConverter<OrderItem, OrderItemResponse> pageConverter;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, PageConverter<OrderItem, OrderItemResponse> pageConverter) {
        this.orderItemRepository = orderItemRepository;
        this.pageConverter = pageConverter;
    }

    @Override
    public Try<OrderItemResponse> createOrderItem(OrderItemRequest orderItemRequest) {
        return Try.of(() -> OrderItemConverter.requestToEntity(orderItemRequest))
                .map(orderItemRepository::save)
                .map(OrderItemConverter::entityToResponse)
                .onSuccess(result -> log.debug("Successfully saved order item={}", result))
                .onFailure(exception -> log.error("Failed to save order item for request={}", orderItemRequest, exception));
    }

    @Override
    public Try<OrderItemResponse> getOrderItemById(Long id) {
        return Try.of(() -> orderItemRepository.findById(id))
                .map(orderItemOptional -> orderItemOptional.map(OrderItemConverter::entityToResponse)
                        .orElseThrow(() -> new OrderItemNotFoundException("Order item not found")))
                .onFailure(error -> log.error("Unable to retrieve order item for id={}", id, error));
    }

    @Override
    public Try<PageResponse<OrderItemResponse>> getAllOrderItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return Try.of(() -> orderItemRepository.findAll(pageable))
                .map(orderItemPage -> pageConverter.toPageResponse(orderItemPage, OrderItemConverter::entityToResponse))
                .onFailure(exception -> log.error("Unable to retrieve order item list for page={} and size={}", page, size, exception))
                .onSuccess(result -> log.debug("Successfully retrieved order items for page={} and size={} ", page, size));
    }

    @Override
    public Try<OrderItemResponse> updateOrderItem(OrderItemRequest updatedOrderItem) {
        var orderItemId = updatedOrderItem.getId();

        return Try.of(() -> orderItemRepository.findById(orderItemId))
                .map(orderItemOptional -> orderItemOptional
                        .map(orderItem -> updateOrderItem(orderItem, updatedOrderItem))
                        .orElseThrow(() -> new OrderItemNotFoundException("Order item not found")))
                .map(OrderItemConverter::entityToResponse)
                .onSuccess(orderItemResponse -> log.debug("Order item with id={} was successfully updated", orderItemId))
                .onFailure(exception -> log.error("Unable to update order item with id={}", orderItemId, exception));
    }

    private OrderItem updateOrderItem(OrderItem existingOrderItem, OrderItemRequest updatedOrderItem) {
        doIfNotNull(updatedOrderItem.getQuantity(), existingOrderItem::setQuantity);
        doIfNotNull(updatedOrderItem.getPrice(), existingOrderItem::setPrice);

        return orderItemRepository.save(existingOrderItem);
    }

    @Override
    public Try<Void> deleteOrderItem(Long id) {
        return Try.run(() -> orderItemRepository.deleteById(id))
                .onSuccess(unused -> log.debug("Successfully deleted order item for id={}", id))
                .onFailure(exception -> log.error("Failed to delete order item for id={}", id));
    }
}
