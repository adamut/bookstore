package com.bookstore.assignment.service;

import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.dao.OrderRepository;
import com.bookstore.assignment.exception.OrderNotFoundException;
import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.request.OrderRequest;
import com.bookstore.assignment.response.OrderResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.util.CustomerConverter;
import com.bookstore.assignment.util.OrderConverter;
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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PageConverter<Order, OrderResponse> pageConverter;

    public OrderServiceImpl(OrderRepository orderRepository, PageConverter<Order, OrderResponse> pageConverter) {
        this.orderRepository = orderRepository;
        this.pageConverter = pageConverter;
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
        doIfNotNull(CustomerConverter.requestToEntity(updatedOrder.getCustomer()), existingOrder::setCustomer);
        doIfNotNull(updatedOrder.getTotalPrice(), existingOrder::setTotalPrice);
        doIfNotNull(updatedOrder.getOrderDate(), existingOrder::setOrderDate);
        doIfNotNull(OrderItemConverter.requestToEntityList(updatedOrder.getItems()), existingOrder::setItems);

        return orderRepository.save(existingOrder);
    }

    @Override
    public Try<OrderResponse> getOrderById(Long id) {
        return Try.of(() -> orderRepository.findById(id))
                .map(orderOptional -> orderOptional.map(OrderConverter::entityToResponse)
                        .orElseThrow(() -> new OrderNotFoundException("Order not found")))
                .onFailure(error -> log.error("Unable to retrieve order for id={}", id, error));
    }

    @Override
    public Try<PageResponse<OrderResponse>> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return Try.of(() -> orderRepository.findAll(pageable))
                .map(orderServicePage -> pageConverter.toPageResponse(orderServicePage, OrderConverter::entityToResponse))
                .onFailure(exception -> log.error("Unable to retrieve order list for page={} and size={}", page, size, exception))
                .onSuccess(result -> log.debug("Successfully retrieved order for page={} and size={} ", page, size));
    }
}
