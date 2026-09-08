package com.example.payment.exchange;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExchangeRate(String currency, LocalDate rateDate, BigDecimal unitsPerUsd) {
}
