package com.bookstore.assignment.controller;

import com.bookstore.assignment.PurchaseResource;
import com.bookstore.assignment.request.PurchaseRequest;
import com.bookstore.assignment.response.PurchaseResponse;
import com.bookstore.assignment.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseResourceImpl implements PurchaseResource {

    private final PurchaseService purchaseService;

    public PurchaseResourceImpl(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Override
    public ResponseEntity<PurchaseResponse> purchaseBooks(PurchaseRequest request) {
        return purchaseService.purchaseBooks(request)
                .map(ResponseEntity::ok)
                .getOrElseGet(error -> ResponseEntity.internalServerError().build());
    }
}
