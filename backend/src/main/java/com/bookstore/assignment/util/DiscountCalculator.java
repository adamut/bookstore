package com.bookstore.assignment.util;

import com.bookstore.assignment.config.DiscountConfig;
import com.bookstore.assignment.model.BookType;
import com.bookstore.assignment.models.Book;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountCalculator {

    private final DiscountConfig discountConfig;

    public DiscountCalculator(DiscountConfig discountConfig) {
        this.discountConfig = discountConfig;
    }

    public BigDecimal applyDiscount(Book book, int boughtItemQuantity) {
        BigDecimal finalPrice = book.getPrice();

        if (book.getType() == BookType.NEW_RELEASE) {
            return finalPrice;
        }

        if (book.getType() == BookType.OLD_EDITION) {
            finalPrice = finalPrice.multiply(discountConfig.getOldEditionDiscount());
            if (boughtItemQuantity >= discountConfig.getOldEditionDiscountQuantity()) {
                return finalPrice.multiply(discountConfig.getOldEditionExtraDiscount());
            }
        }

        if (book.getType() == BookType.REGULAR && boughtItemQuantity >= discountConfig.getRegularDiscountQuantity()) {
            return finalPrice.multiply(discountConfig.getRegularDiscount());
        }

        return finalPrice;
    }
}
