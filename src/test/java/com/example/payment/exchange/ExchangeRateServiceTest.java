package com.example.payment.exchange;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ExchangeRateServiceTest {
    @Test
    void selectsLatestRateOnOrBeforePurchaseDateWithinSixMonths() {
        ExchangeRateClient client = (currency, date) -> List.of(
                rate("2026-01-01", "0.80"),
                rate("2026-06-01", "0.90"),
                rate("2026-08-16", "0.95"));

        Optional<ExchangeRate> result = new ExchangeRateService(client)
                .findRate("EUR", LocalDate.of(2026, 8, 15));

        assertThat(result).isPresent().get().extracting(ExchangeRate::rateDate)
                .isEqualTo(LocalDate.of(2026, 6, 1));
    }

    @Test
    void returnsEmptyWhenTheNewestRateIsOlderThanSixMonths() {
        ExchangeRateClient client = (currency, date) -> List.of(rate("2025-12-01", "0.80"));

        Optional<ExchangeRate> result = new ExchangeRateService(client)
                .findRate("EUR", LocalDate.of(2026, 8, 15));

        assertThat(result).isEmpty();
    }

    private ExchangeRate rate(String date, String value) {
        return new ExchangeRate("EUR", LocalDate.parse(date), new BigDecimal(value));
    }
}
