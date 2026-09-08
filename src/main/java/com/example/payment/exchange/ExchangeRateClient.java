package com.example.payment.exchange;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateClient {
    List<ExchangeRate> findRates(String currency, LocalDate onOrBefore);
}
