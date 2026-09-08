package com.example.payment.purchase.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PurchaseConversionResponse(
        UUID id,
        String description,
        LocalDate transactionDate,
        BigDecimal amountUsd,
        String targetCurrency,
        LocalDate exchangeRateDate,
        BigDecimal exchangeRate,
        BigDecimal convertedAmount) {
}
