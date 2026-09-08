package com.example.payment.shared;

import java.time.LocalDate;

public class ConversionUnavailableException extends RuntimeException {
    public ConversionUnavailableException(String currency, LocalDate purchaseDate) {
        super("Purchase cannot be converted to " + currency + " for " + purchaseDate
                + ": no exchange rate is available within the previous six months");
    }
}
