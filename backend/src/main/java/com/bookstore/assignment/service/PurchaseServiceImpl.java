package com.bookstore.assignment.service;

import com.bookstore.assignment.config.LoyaltyPointsConfig;
import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.exception.CustomerNotFoundException;
import com.bookstore.assignment.dao.BookRepository;
import com.bookstore.assignment.dao.CustomerRepository;
import com.bookstore.assignment.dao.OrderItemRepository;
import com.bookstore.assignment.dao.OrderRepository;
import com.bookstore.assignment.exception.NotEnoughStockException;
import com.bookstore.assignment.model.BookType;
import com.bookstore.assignment.models.Customer;
import com.bookstore.assignment.models.PurchaseContext;
import com.bookstore.assignment.models.Book;
import com.bookstore.assignment.models.Order;
import com.bookstore.assignment.models.OrderItem;
import com.bookstore.assignment.request.BookOrderItem;
import com.bookstore.assignment.request.PurchaseRequest;
import com.bookstore.assignment.response.PurchaseResponse;
import com.bookstore.assignment.util.BookUtil;
import com.bookstore.assignment.util.DiscountCalculator;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DiscountCalculator discountCalculator;
    private final LoyaltyPointsConfig loyaltyPointsConfig;

    public PurchaseServiceImpl(BookRepository bookRepository,
                               CustomerRepository customerRepository,
                               OrderRepository orderRepository,
                               OrderItemRepository orderItemRepository,
                               DiscountCalculator discountCalculator,
                               LoyaltyPointsConfig loyaltyPointsConfig) {
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.discountCalculator = discountCalculator;
        this.loyaltyPointsConfig = loyaltyPointsConfig;
    }

    @Transactional
    @Override
    public Try<PurchaseResponse> purchaseBooks(PurchaseRequest request) {
        return Try.of(() -> getCustomer(request))
                .map(customer -> PurchaseContext.of(request, customer))
                .flatMap(this::processPurchase)
                .onSuccess(result -> log.info("Successfully purchased order with result={}", result))
                .onFailure(exception -> log.error("Failed to purchase order for request={}", request, exception));
    }

    private Customer getCustomer(PurchaseRequest request) {
        return customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found for customerId=" + request.getCustomerId()));
    }

    private Try<PurchaseResponse> processPurchase(PurchaseContext context) {
        return getBooksForOrder(context)
                .flatMap(this::saveInitialOrder)
                .flatMap(this::saveOrderItems)
                .map(this::calculateTotalPrice)
                .map(this::calculateEarnedLoyaltyPoints)
                .flatMap(this::updateOrder)
                .flatMap(this::updateCustomerLoyaltyPoints)
                .flatMap(this::updateBooksStock)
                .map(this::buildPurchaseResponse);
    }


    private Try<PurchaseContext> saveInitialOrder(PurchaseContext context) {
        Order newOrder = new Order();
        newOrder.setCustomer(context.getCustomer());
        newOrder.setTotalPrice(BigDecimal.ZERO);

        return Try.of(() -> orderRepository.save(newOrder))
                .map(order -> {
                    context.setOrder(order);
                    return context;
                })
                .onFailure(exception -> log.error("Failed to save initial order for order={}", newOrder, exception))
                .onSuccess(result -> log.debug("Successfully saved initial order before adding items for result={}", result));
    }

    private Try<PurchaseContext> getBooksForOrder(PurchaseContext context) {
        var bookIds = context.getRequest().getBooks().stream()
                .map(BookOrderItem::getBookId)
                .toList();

        return Try.of(() -> bookRepository.findAllById(bookIds))
                .map(list -> {
                    context.setOrderBooks(list);
                    return context;
                })
                .onFailure(exception -> log.error("Failed to retrieve books for book ids={}", bookIds, exception));
    }

    private Try<PurchaseContext> saveOrderItems(PurchaseContext purchaseContext) {
        return Try.of(() -> purchaseContext.getRequest().getBooks().stream()
                        .map(item -> buildOrderItem(purchaseContext, item))
                        .toList())
                .flatMap(orderItems -> Try.of(() -> orderItemRepository.saveAll(orderItems)))
                .map(orderItemList -> {
                    purchaseContext.setOrderItemList(orderItemList);
                    return purchaseContext;
                })
                .onFailure(exception -> log.error("Unable to save orderItems={}", purchaseContext.getOrderItemList(), exception))
                .onSuccess(result -> log.debug("Successfully saved orderItems={}", purchaseContext.getOrderItemList()));
    }

    private OrderItem buildOrderItem(PurchaseContext purchaseContext, BookOrderItem item) {
        return Try.of(() -> purchaseContext.getOrderBooks().stream()
                        .filter(book -> book.getId().equals(item.getBookId()))
                        .findFirst()
                        .orElseThrow(() -> new BookNotFoundException("Book not found")))
                .flatMap(book -> isEligibleBookToPurchase(book, item))
                .andThen(book -> isEligibleForDiscount(purchaseContext, book, item))
                .flatMap(book -> buildOrderItem(purchaseContext, item, book))
                .andThen(el -> setBooksToUpdate(purchaseContext, el))
                .get();
    }

    private static Try<Book> isEligibleBookToPurchase(Book book, BookOrderItem item) {
        return book.getStock() >= item.getQuantity()
                ? Try.success(book)
                : Try.failure(new NotEnoughStockException("Not enough stock for book with id= " + book.getId()));
    }

    private void isEligibleForDiscount(PurchaseContext purchaseContext, Book book, BookOrderItem item) {
        if (book.getType() == BookType.REGULAR || book.getType() == BookType.OLD_EDITION) {
            purchaseContext.getEligibleForDiscount().put(book, item.getQuantity());
        }
    }

    private Try<OrderItem> buildOrderItem(PurchaseContext purchaseContext, BookOrderItem item, Book book) {
        Order order = purchaseContext.getOrder();
        return Try.of(() -> {
                    BigDecimal bookPrice = discountCalculator.applyDiscount(book, item.getQuantity());

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(book);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(bookPrice);

                    return orderItem;
                })
                .onFailure(exception -> log.error("Failed to build orderItem for orderId={} and item={}", order.getId(), item, exception));
    }

    private void setBooksToUpdate(PurchaseContext context, OrderItem orderItem) {
        Book book = orderItem.getBook();
        book.setStock(book.getStock() - orderItem.getQuantity());
        context.getBooksToUpdate().add(book);
    }

    private Try<PurchaseContext> updateCustomerLoyaltyPoints(PurchaseContext context) {
        Customer customer = context.getCustomer();
        int existingLoyaltyPoints = customer.getLoyaltyPoints();
        customer.setLoyaltyPoints(existingLoyaltyPoints + context.getEarnedLoyaltyPoints());

        return Try.of(() -> customerRepository.save(customer))
                .map(updatedCustomer -> {
                    context.setCustomer(updatedCustomer);
                    return context;
                })
                .onFailure(exception -> log.error("Failed to update customer loyalty points for customer={}", customer, exception))
                .onSuccess(result -> log.debug("Successfully updated customer's loyalty points for customer={}", result.getCustomer()));
    }

    private Try<PurchaseContext> updateOrder(PurchaseContext context) {
        Order order = context.getOrder();
        order.setTotalPrice(context.getTotalPrice());

        return Try.of(() -> orderRepository.save(order))
                .map(savedOrder -> {
                    context.setOrder(savedOrder);
                    return context;
                })
                .onFailure(exception -> log.error("Failed to update order for order={}", order, exception));
    }

    private PurchaseContext calculateTotalPrice(PurchaseContext context) {
        BigDecimal totalPrice = context.getOrderItemList().stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Customer customer = context.getCustomer();

        int existingLoyaltyPoints = customer.getLoyaltyPoints();
        Map<Book, Integer> eligibleForDiscount = context.getEligibleForDiscount();

        while (existingLoyaltyPoints >= loyaltyPointsConfig.getPointsThreshold() && !eligibleForDiscount.isEmpty()) {
            Book bookToDiscount = eligibleForDiscount.entrySet().stream()
                    .min(Comparator.comparing(entry -> entry.getKey().getPrice()))
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (bookToDiscount == null) {
                //no more eligible books for loyalty discount
                break;
            }

            int remainingBookQuantity = eligibleForDiscount.get(bookToDiscount);

            if (remainingBookQuantity > 1) {
                eligibleForDiscount.put(bookToDiscount, remainingBookQuantity - 1);
            } else {
                eligibleForDiscount.remove(bookToDiscount);
            }
            BookOrderItem orderItemToDiscount = BookUtil.findOrderItemByBookId(context.getRequest().getBooks(), bookToDiscount.getId());
            BigDecimal deductedPrice = discountCalculator.applyDiscount(bookToDiscount, orderItemToDiscount.getQuantity());
            totalPrice = totalPrice.subtract(deductedPrice);

            existingLoyaltyPoints -= loyaltyPointsConfig.getPointsThreshold();
            customer.setLoyaltyPoints(existingLoyaltyPoints);
        }

        context.setTotalPrice(totalPrice);
        return context;
    }

    private PurchaseContext calculateEarnedLoyaltyPoints(PurchaseContext context) {
        int earnedLoyaltyPoints = context.getOrderItemList().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        context.setEarnedLoyaltyPoints(earnedLoyaltyPoints);
        return context;
    }

    private Try<PurchaseContext> updateBooksStock(PurchaseContext context) {
        return Try.of(() -> bookRepository.saveAll(context.getBooksToUpdate()))
                .map(result -> context)
                .onSuccess(result -> log.debug("Successfully updated books stock after placing order"))
                .onFailure(exception -> log.error("Failed to update stock on books after placing order"));
    }

    private PurchaseResponse buildPurchaseResponse(PurchaseContext context) {
        return new PurchaseResponse(context.getOrder().getId(), context.getTotalPrice(), context.getEarnedLoyaltyPoints(), context.getCustomer().getLoyaltyPoints());
    }
}