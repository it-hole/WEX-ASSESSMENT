package com.example.payment.purchase;

import com.example.payment.purchase.dto.CreatePurchaseRequest;
import com.example.payment.purchase.dto.PurchaseResponse;
import com.example.payment.purchase.dto.PurchaseConversionResponse;
import com.example.payment.exchange.ExchangeRate;
import com.example.payment.exchange.ExchangeRateService;
import com.example.payment.shared.ConversionUnavailableException;
import com.example.payment.shared.PurchaseNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {
    private static final int MONEY_SCALE = 2;

    private final PurchaseRepository purchaseRepository;
    private final ExchangeRateService exchangeRateService;

    public PurchaseService(PurchaseRepository purchaseRepository, ExchangeRateService exchangeRateService) {
        this.purchaseRepository = purchaseRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Transactional
    public PurchaseResponse create(CreatePurchaseRequest request) {
        BigDecimal amount = request.amountUsd().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amountUsd must round to a positive amount");
        }
        Purchase purchase = purchaseRepository.save(
                new Purchase(request.description().trim(), request.transactionDate(), amount));
        return toResponse(purchase);
    }

    @Transactional(readOnly = true)
    public PurchaseConversionResponse convert(UUID id, String currency) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(() -> new PurchaseNotFoundException(id));
        String targetCurrency = currency.trim().toUpperCase();
        ExchangeRate rate = exchangeRateService.findRate(targetCurrency, purchase.getTransactionDate())
                .orElseThrow(() -> new ConversionUnavailableException(targetCurrency, purchase.getTransactionDate()));
        BigDecimal convertedAmount = purchase.getAmountUsd()
                .multiply(rate.unitsPerUsd())
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        return new PurchaseConversionResponse(purchase.getId(), purchase.getDescription(), purchase.getTransactionDate(),
                purchase.getAmountUsd(), targetCurrency, rate.rateDate(), rate.unitsPerUsd(), convertedAmount);
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        return new PurchaseResponse(purchase.getId(), purchase.getDescription(), purchase.getTransactionDate(),
                purchase.getAmountUsd());
    }
}
