package com.bookstore.assignment.service;

import com.bookstore.assignment.dao.BookRepository;
import com.bookstore.assignment.models.Book;
import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.response.BookResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.util.BookConverter;
import com.bookstore.assignment.util.PageConverter;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PageConverter<Book, BookResponse> pageConverter;

    public BookServiceImpl(BookRepository bookRepository, PageConverter<Book, BookResponse> pageConverter) {
        this.bookRepository = bookRepository;
        this.pageConverter = pageConverter;
    }

    @Override
    public Try<BookResponse> createBook(BookRequest bookRequest) {
        return Try.of(() -> BookConverter.requestToEntity(bookRequest))
                .map(bookRepository::save)
                .map(BookConverter::entityToResponse)
                .onSuccess(result -> log.debug("Successfully stored book={}", result))
                .onFailure(exception -> log.error("Failed to save book for request={}", bookRequest, exception));
    }

    @Override
    public Try<BookResponse> getBookById(Long id) {
        return Try.of(() -> bookRepository.findById(id))
                .map(bookOptional -> bookOptional.map(BookConverter::entityToResponse)
                        .orElseThrow(() -> new BookNotFoundException("Book not found")))
                .onFailure(error -> log.error("Unable to retrieve book for id={}", id, error));
    }

    @Override
    public Try<PageResponse<BookResponse>> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return Try.of(() -> bookRepository.findAll(pageable))
                .map(bookPage -> pageConverter.toPageResponse(bookPage, BookConverter::entityToResponse))
                .onFailure(exception -> log.error("Unable to retrieve book list for page={} and size={}", page, size, exception))
                .onSuccess(result -> log.debug("Successfully retrieved books for page={} and size={} ", page, size));
    }

    @Override
    public Try<BookResponse> updateBook(BookRequest updatedBook) {
        var bookId = updatedBook.getId();

        return Try.of(() -> bookRepository.findById(bookId))
                .map(bookOptional -> bookOptional
                        .map(book -> updateBook(book, updatedBook))
                        .orElseThrow(() -> new BookNotFoundException("Book not found")))
                .map(BookConverter::entityToResponse)
                .onSuccess(book -> log.debug("Book with id={} was successfully updated", bookId))
                .onFailure(exception -> log.error("Unable to update book with id={}", bookId, exception));
    }

    private Book updateBook(Book existingBook, BookRequest updatedBook) {
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setType(updatedBook.getType());
        existingBook.setStock(updatedBook.getStock());
        existingBook.setUpdatedAt(Instant.now());

        return bookRepository.save(existingBook);
    }

    @Override
    public Try<Void> deleteBook(Long id) {
        return Try.run(() -> bookRepository.deleteById(id))
                .onSuccess(unused -> log.debug("Successfully deleted book for id={}", id))
                .onFailure(exception -> log.error("Failed to delete book for id={}", id));
    }
}
