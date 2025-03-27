package com.bookstore.assignment;

import com.bookstore.assignment.request.PurchaseRequest;
import com.bookstore.assignment.response.PurchaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("bookstore/purchase")
public interface PurchaseResource {

    @PostMapping
    ResponseEntity<PurchaseResponse> purchaseBooks(@RequestBody PurchaseRequest request);
}
