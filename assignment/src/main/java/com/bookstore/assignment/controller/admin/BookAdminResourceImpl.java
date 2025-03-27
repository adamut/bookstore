package com.bookstore.assignment.controller.admin;

import com.bookstore.assignment.admin.BookAdminResource;
import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.response.BookResponse;
import com.bookstore.assignment.service.BookService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookAdminResourceImpl implements BookAdminResource {

    private final BookService bookService;

    public BookAdminResourceImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public ResponseEntity<BookResponse> createBook(@NotNull BookRequest bookRequest) {
        return bookService.createBook(bookRequest)
                .map(ResponseEntity::ok)
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }

    @Override
    public ResponseEntity<BookResponse> updateBook(@NotNull BookRequest bookRequest) {
        return bookService.updateBook(bookRequest)
                .map(ResponseEntity::ok)
                .recover(BookNotFoundException.class, cause -> ResponseEntity.notFound().build())
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }

    @Override
    public ResponseEntity<Void> deleteBook(@NotNull Long id) {
        return bookService.deleteBook(id)
                .map(ResponseEntity::ok)
                .getOrElseGet(exception -> ResponseEntity.internalServerError().build());
    }
}
