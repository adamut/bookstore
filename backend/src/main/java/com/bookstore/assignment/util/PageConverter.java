package com.bookstore.assignment.util;

import com.bookstore.assignment.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;


@Component
public class PageConverter<T, V> {


    public PageResponse<V> toPageResponse(Page<T> page, Function<T, V> converterFunction) {
        List<V> convertedList = page.stream()
                .map(converterFunction)
                .toList();

        return PageResponse.<V>builder()
                .content(convertedList)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
