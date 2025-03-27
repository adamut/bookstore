package com.bookstore.assignment.service;

import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.response.BookResponse;
import com.bookstore.assignment.response.PageResponse;
import io.vavr.control.Try;


public interface BookService {

    Try<BookResponse> createBook(BookRequest bookRequest);

    Try<BookResponse> getBookById(Long id);

    Try<PageResponse<BookResponse>> getAllBooks(int page, int size);

    Try<BookResponse> updateBook(BookRequest updatedBook);

    Try<Void> deleteBook(Long id);
}
