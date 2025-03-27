package com.bookstore.assignment.controller;

import com.bookstore.assignment.BookResource;
import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.response.BookResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.service.BookService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookResourceImpl implements BookResource {

    private final BookService bookService;

    public BookResourceImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public PageResponse<BookResponse> getBooks(@NotNull Integer page, @NotNull Integer size) {
        return bookService.getAllBooks(page, size)
                .get();
    }

    @Override
    public ResponseEntity<BookResponse> getBookById(@NotNull Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .recover(BookNotFoundException.class, cause -> ResponseEntity.notFound().build())
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }
}
