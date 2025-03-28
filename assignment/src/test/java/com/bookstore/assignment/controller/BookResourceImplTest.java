package com.bookstore.assignment.controller;

import com.bookstore.assignment.exception.BookNotFoundException;
import com.bookstore.assignment.response.BookResponse;
import com.bookstore.assignment.response.PageResponse;
import com.bookstore.assignment.service.BookService;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookResourceImplTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookResourceImpl bookResource;

    @Test
    void getBooks_ShouldReturnPageResponse() {
        // Given
        int page = 0;
        int size = 10;
        PageResponse<BookResponse> expectedResponse = new PageResponse<>();
        when(bookService.getAllBooks(page, size)).thenReturn(Try.success(expectedResponse));

        // When
        PageResponse<BookResponse> actualResponse = bookResource.getBooks(page, size);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(bookService).getAllBooks(page, size);
    }

    @Test
    void getBookById_ShouldReturnBookResponse() {
        // Given
        Long bookId = 1L;
        BookResponse bookResponse = new BookResponse();
        when(bookService.getBookById(bookId)).thenReturn(Try.success(bookResponse));

        // When
        ResponseEntity<BookResponse> response = bookResource.getBookById(bookId);

        // Then
        assertNotNull(response);
        assertEquals(ResponseEntity.ok(bookResponse), response);
        verify(bookService).getBookById(bookId);
    }

    @Test
    void getBookById_ShouldReturnNotFound_WhenBookNotExists() {
        // Given
        Long bookId = 99L;
        when(bookService.getBookById(bookId)).thenReturn(Try.failure(new BookNotFoundException("Book not found")));

        // When
        ResponseEntity<BookResponse> response = bookResource.getBookById(bookId);

        // Then
        assertNotNull(response);
        assertEquals(ResponseEntity.notFound().build(), response);
        verify(bookService).getBookById(bookId);
    }

    @Test
    void getBookById_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() {
        // Given
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(Try.failure(new RuntimeException("Unexpected error")));

        // When
        ResponseEntity<BookResponse> response = bookResource.getBookById(bookId);

        // Then
        assertNotNull(response);
        assertEquals(ResponseEntity.internalServerError().build(), response);
        verify(bookService).getBookById(bookId);
    }
}
