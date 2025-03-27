package com.bookstore.assignment.dao;

import com.bookstore.assignment.models.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    void deleteByAddedAtBefore(Instant date);
}
