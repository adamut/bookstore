package com.bookstore.assignment.response;

import com.bookstore.assignment.model.BookType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private BookType type;
    private int stock;
    private Instant addedAt;
    private Instant updatedAt;
}
