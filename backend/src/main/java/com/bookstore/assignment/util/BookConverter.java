package com.bookstore.assignment.util;

import com.bookstore.assignment.models.Book;
import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.response.BookResponse;

public class BookConverter {

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

    public static Book requestToEntity(BookRequest bookRequest){
        Book book = new Book();
        book.setId(bookRequest.getId());
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setPrice(bookRequest.getPrice());
        book.setType(bookRequest.getType());
        book.setStock(bookRequest.getStock());
        book.setAddedAt(bookRequest.getAddedAt());
        book.setUpdatedAt(bookRequest.getUpdatedAt());

        return book;
    }
}
