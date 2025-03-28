package com.bookstore.assignment.util;

import com.bookstore.assignment.exception.OrderItemNotFoundException;
import com.bookstore.assignment.request.BookOrderItem;

import java.util.List;

public class BookUtil {

    public static BookOrderItem findOrderItemByBookId(List<BookOrderItem> orderItemRequestList, Long id) {
        return orderItemRequestList.stream()
                .filter(orderItemRequest -> id.equals(orderItemRequest.getBookId()))
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException("Book order item request not found for bookId=" + id));
    }
}
