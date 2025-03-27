package com.bookstore.assignment.util;

import com.bookstore.assignment.models.Book;
import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.response.BookResponse;


import static com.bookstore.assignment.util.ConverterUtil.doIfNotNull;

public class BookConverter {

    private BookConverter() {
    }

    public static BookResponse entityToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .price(book.getPrice())
                .type(book.getType())
                .stock(book.getStock())
                .addedAt(book.getAddedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    public static Book requestToEntity(BookRequest bookRequest) {
        Book book = new Book();

        doIfNotNull(bookRequest.getId(), book::setId);
        doIfNotNull(bookRequest.getTitle(), book::setTitle);
        doIfNotNull(bookRequest.getDescription(), book::setDescription);
        doIfNotNull(bookRequest.getPrice(), book::setPrice);
        doIfNotNull(bookRequest.getType(), book::setType);
        doIfNotNull(bookRequest.getStock(), book::setStock);
        doIfNotNull(bookRequest.getAddedAt(), book::setAddedAt);
        doIfNotNull(bookRequest.getUpdatedAt(), book::setUpdatedAt);

        return book;
    }
}
