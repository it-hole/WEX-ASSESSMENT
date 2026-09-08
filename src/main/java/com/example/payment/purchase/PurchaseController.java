package com.example.payment.purchase;

import com.example.payment.purchase.dto.CreatePurchaseRequest;
import com.example.payment.purchase.dto.PurchaseConversionResponse;
import com.example.payment.purchase.dto.PurchaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
@Validated
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> create(@Valid @RequestBody CreatePurchaseRequest request) {
        PurchaseResponse response = purchaseService.create(request);
        return ResponseEntity.created(URI.create("/api/purchases/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public PurchaseConversionResponse getConverted(@PathVariable UUID id,
            @RequestParam @NotBlank String currency) {
        return purchaseService.convert(id, currency);
    }
}
