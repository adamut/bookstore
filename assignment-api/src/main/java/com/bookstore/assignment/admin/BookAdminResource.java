package com.bookstore.assignment.admin;

import com.bookstore.assignment.request.BookRequest;
import com.bookstore.assignment.response.BookResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("bookstore/admin/book")
public interface BookAdminResource {


    @PostMapping
    ResponseEntity<BookResponse> createBook(@NotNull @RequestBody BookRequest bookRequest);

    @PutMapping
    ResponseEntity<BookResponse> updateBook(@NotNull @RequestBody BookRequest bookRequest);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBook(@NotNull @PathVariable("id") Long id);
}
