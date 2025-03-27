package com.bookstore.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
@ConfigurationProperties(prefix = "discount.config")
public class DiscountConfig {

    private BigDecimal oldEditionDiscount;

    private BigDecimal oldEditionExtraDiscount;

    private Integer oldEditionDiscountQuantity;

    private BigDecimal regularDiscount;

    private Integer regularDiscountQuantity;
}
