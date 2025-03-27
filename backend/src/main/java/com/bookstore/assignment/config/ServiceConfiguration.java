package com.bookstore.assignment.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServiceConfiguration {

    private final BookConfiguration bookConfiguration;
}
