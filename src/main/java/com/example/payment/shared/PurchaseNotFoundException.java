package com.example.payment.shared;

import java.util.UUID;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(UUID id) {
        super("Purchase " + id + " was not found");
    }
}
