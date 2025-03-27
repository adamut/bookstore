package com.bookstore.assignment.service;

import com.bookstore.assignment.dao.BookRepository;
import com.bookstore.assignment.config.BookConfiguration;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class InventoryCleanupService {

    private final BookRepository bookRepository;
    private final BookConfiguration bookConfiguration;

    public InventoryCleanupService(BookRepository bookRepository, BookConfiguration bookConfiguration) {
        this.bookRepository = bookRepository;
        this.bookConfiguration = bookConfiguration;
    }

    @Scheduled(cron = "0 0 2 * * ?") //Runs daily at 2 AM
    public void removeOldBooks() {
        var amountToSubtract = bookConfiguration.getBookExpirationMonths();
        var thresholdDate = Instant.now().minus(amountToSubtract, ChronoUnit.MONTHS);

        log.info("Started to remove books older than months={} for date={}", amountToSubtract, thresholdDate);
        Try.run(() -> bookRepository.deleteByAddedAtBefore(thresholdDate))
                .onSuccess(unused -> log.info("Deleted books added before date={}", thresholdDate))
                .onFailure(error -> log.error("Failed to delete books added before date={}", thresholdDate, error));
    }
}
