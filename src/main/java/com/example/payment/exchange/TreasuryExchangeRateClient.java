package com.example.payment.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TreasuryExchangeRateClient implements ExchangeRateClient {
    private final RestClient restClient;

    public TreasuryExchangeRateClient(RestClient.Builder builder,
            @Value("${treasury.api-base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public List<ExchangeRate> findRates(String currency, LocalDate onOrBefore) {
        TreasuryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/services/api/fiscal_service/v1/accounting/od/rates_of_exchange")
                        .queryParam("fields", "record_date,currency,exchange_rate,country_currency_desc")
                        .queryParam("filter", "record_date:lte:" + onOrBefore)
                        .queryParam("sort", "-record_date")
                        .queryParam("page[size]", 1000)
                        .build())
                .retrieve()
                .body(TreasuryResponse.class);
        if (response == null || response.data() == null) {
            return List.of();
        }
        String requested = currency.trim().toUpperCase(Locale.ROOT);
        return response.data().stream()
                .filter(rate -> currencyMatches(requested, rate))
                .map(rate -> new ExchangeRate(requested, LocalDate.parse(rate.recordDate()), rate.exchangeRate()))
                .toList();
    }

    private boolean currencyMatches(String requested, TreasuryRate rate) {
        String name = Objects.toString(rate.currency(), "").trim();
        String description = Objects.toString(rate.countryCurrencyDescription(), "").trim();
        return requested.equalsIgnoreCase(name)
                || requested.equalsIgnoreCase(description)
                || requested.equalsIgnoreCase(currencyCode(name))
                || requested.equalsIgnoreCase(currencyCode(description));
    }

    private String currencyCode(String value) {
        return Map.ofEntries(
                Map.entry("EURO", "EUR"),
                Map.entry("POUND STERLING", "GBP"),
                Map.entry("JAPANESE YEN", "JPY"),
                Map.entry("CANADIAN DOLLAR", "CAD"),
                Map.entry("AUSTRALIAN DOLLAR", "AUD"),
                Map.entry("SWISS FRANC", "CHF"),
                Map.entry("MEXICAN PESO", "MXN"),
                Map.entry("INDIAN RUPEE", "INR"))
                .getOrDefault(value.toUpperCase(Locale.ROOT), value);
    }

    private record TreasuryResponse(List<TreasuryRate> data) { }

    private record TreasuryRate(
            @JsonProperty("record_date") String recordDate,
            String currency,
            @JsonProperty("exchange_rate") BigDecimal exchangeRate,
            @JsonProperty("country_currency_desc") String countryCurrencyDescription) { }
}
