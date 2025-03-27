package com.bookstore.assignment.service;

import com.bookstore.assignment.request.PurchaseRequest;
import com.bookstore.assignment.response.PurchaseResponse;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;

public interface PurchaseService {

    @Transactional
    Try<PurchaseResponse> purchaseBooks(PurchaseRequest request);
}
