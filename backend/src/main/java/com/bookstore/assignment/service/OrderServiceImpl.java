package com.bookstore.assignment.service;

import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.dao.OrderRepository;
import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;
import com.bookstore.assignment.util.CustomerConverter;
import com.bookstore.assignment.util.OrderConverter;
import com.bookstore.assignment.util.OrderItemsConverter;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Try<OrderResponse> createOrder(OrderRequest orderRequest) {
        return Try.of(() -> OrderConverter.requestToEntity(orderRequest))
                .map(orderRepository::save)
                .map(OrderConverter::entityToResponse)
                .onSuccess(result -> log.debug("Successfully saved order={}", result))
                .onFailure(exception -> log.error("Failed to save order for request={}", orderRequest, exception));
    }

    @Override
    public Try<OrderResponse> updateOrder(OrderRequest orderRequest) {
        var orderRequestId = orderRequest.getId();

        return Try.of(() -> orderRepository.findById(orderRequestId))
                .map(orderOptional -> orderOptional
                        .map(order -> updateOrder(order, orderRequest))
                        .orElseThrow(() -> new CustomerNotFoundException("Order not found")))
                .map(OrderConverter::entityToResponse)
                .onSuccess(customerResponse -> log.debug("Order with id={} was successfully updated", orderRequestId))
                .onFailure(exception -> log.error("Order to update customer with id={}", orderRequestId, exception));
    }

    private Order updateOrder(Order existingOrder, OrderRequest updatedOrder) {
        existingOrder.setCustomer(CustomerConverter.requestToEntity(updatedOrder.getCustomer()));
        existingOrder.setTotalPrice(updatedOrder.getTotalPrice());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        existingOrder.setItems(OrderItemsConverter.requestToEntityList(updatedOrder.getItems()));

        return orderRepository.save(existingOrder);
    }

}
