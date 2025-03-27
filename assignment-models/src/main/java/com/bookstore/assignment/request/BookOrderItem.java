package com.bookstore.assignment.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookOrderItem {

    @NotNull
    private Long bookId;

    @NotNull
    private int quantity;
}
