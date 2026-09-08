package com.example.payment.purchase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "amount_usd", nullable = false, precision = 19, scale = 2)
    private BigDecimal amountUsd;

    protected Purchase() {
    }

    public Purchase(String description, LocalDate transactionDate, BigDecimal amountUsd) {
        this.description = description;
        this.transactionDate = transactionDate;
        this.amountUsd = amountUsd;
    }

    public UUID getId() { return id; }
    public String getDescription() { return description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public BigDecimal getAmountUsd() { return amountUsd; }
}
