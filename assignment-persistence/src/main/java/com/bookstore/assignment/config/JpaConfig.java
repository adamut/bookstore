package com.bookstore.assignment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.bookstore.assignment.dao")
public class JpaConfig {
}
