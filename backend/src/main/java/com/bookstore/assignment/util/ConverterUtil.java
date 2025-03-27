package com.bookstore.assignment.util;

import java.util.function.Consumer;

public class ConverterUtil {

    public static <T> void doIfNotNull(T value, Consumer<T> consumer) {
        if (null != value) {
            consumer.accept(value);
        }
    }
}