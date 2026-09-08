CREATE TABLE purchases (
    id UUID PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL,
    amount_usd NUMERIC(19, 2) NOT NULL CHECK (amount_usd > 0)
);
