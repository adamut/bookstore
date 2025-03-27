package com.bookstore.assignment.request;

import com.bookstore.assignment.model.BookType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BookType type;

    @NotNull
    private int stock;

    private Instant addedAt;
    private Instant updatedAt;
}
