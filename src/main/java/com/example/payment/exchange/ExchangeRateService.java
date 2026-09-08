package com.example.payment.exchange;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
    private final ExchangeRateClient exchangeRateClient;

    public ExchangeRateService(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    public Optional<ExchangeRate> findRate(String currency, LocalDate purchaseDate) {
        LocalDate earliestAllowedDate = purchaseDate.minusMonths(6);
        return exchangeRateClient.findRates(currency, purchaseDate).stream()
                .filter(rate -> !rate.rateDate().isAfter(purchaseDate))
                .filter(rate -> !rate.rateDate().isBefore(earliestAllowedDate))
                .max(java.util.Comparator.comparing(ExchangeRate::rateDate));
    }
}
