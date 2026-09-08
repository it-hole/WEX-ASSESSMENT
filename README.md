# Payment Service

A Java 21 Spring Boot service for storing USD purchase transactions and converting them using Treasury Reporting Rates of Exchange data.

## Run locally

Start PostgreSQL:

```powershell
docker compose up -d
```

Run the application:

```powershell
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. OpenAPI documentation is available at `/swagger-ui.html`.

## API

Create a purchase:

```powershell
curl -X POST http://localhost:8080/api/purchases `
  -H "Content-Type: application/json" `
  -d '{"description":"Office supplies","transactionDate":"2026-08-15","amountUsd":"125.50"}'
```

Convert a stored purchase:

```powershell
curl http://localhost:8080/api/purchases/{id}?currency=EUR
```

Conversion uses the latest Treasury rate on or before the purchase date, provided that rate is no more than six months old. Amounts use `BigDecimal` and are rounded to two decimal places.

## Test

```powershell
mvn test
```

Database settings and the Treasury base URL can be overridden with `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, and `TREASURY_API_BASE_URL`.
