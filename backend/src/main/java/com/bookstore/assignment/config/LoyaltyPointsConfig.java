package com.bookstore.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "loyalty")
public class LoyaltyPointsConfig {

    private Integer pointsThreshold;
}
