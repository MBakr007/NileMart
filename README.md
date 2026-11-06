# NileMart

Console-only Spring Framework project for **NileMart Assignment 1 – Spring Framework
Fundamentals (Sessions 1 & 2)**. The deliverable lives in `nile-mart-core` and uses
plain Spring (no Boot, no web, no database).

## Build and test

All commands are run from `nile-mart-core`:

```bash
mvn clean test          # compiles everything and runs the 23 verification tests
mvn -q clean compile    # compile only
```

## Run the console app

```bash
mvn exec:java            # runs org.route.Main
```

Or run `org.route.Main` from your IDE. Use option `0` to exit so the
`@PreDestroy` hooks print the end-of-day summary and `Catalog closed`.

## Run the XML/Java wiring demo

```bash
mvn exec:java -Dexec.mainClass=org.route.WiringDemo
```

Both the hand-written `ObjectFactory` and the `beans.xml` context print:

```text
Java factory (Giza, 4350.00): 50.00
XML context  (Giza, 4350.00): 50.00
```

## Switching pricing strategy

`StandardPricingService` is `@Primary` by default. To use promotional pricing,
move the `@Primary` annotation from `StandardPricingService` to
`PromotionalPricingService`. The discount percentage is configured in
`src/main/resources/application.properties` (`store.discount-amount=20`).

## Expected checkout results

| # | Cart | Governorate | Subtotal | VAT | Shipping | Total |
|---|------|-------------|----------|-----|----------|-------|
| 1 | 2 x Fast Charger 20W + 1 x Cotton Galabeya | Giza (standard) | 2,200.00 | 308.00 | 50.00 | 2,558.00 |
| 2 | Same cart, promotional 20% | Giza | 1,760.00 | 246.40 | 50.00 | 2,056.40 |
| 3 | 1 x Smartphone X5 128GB | Aswan | 16,000.00 | 2,240.00 | 0.00 | 18,240.00 |
| 4 | 1 x Smartphone X5 128GB | South Sinai | 16,000.00 | 2,240.00 | 120.00 | 18,360.00 |

## Implementation notes

- Money is always `BigDecimal` with scale 2 and `RoundingMode.HALF_UP`.
- Business-rule failures throw `StoreException`.
- All dependencies use constructor injection; `ShoppingCart` is a prototype bean.
- `ExecutionLog` times every `*Service` method; `AuditLog` logs checkout attempts.
- Seed catalogue (used by the console): Smartphone X5 128GB, Fast Charger 20W,
  Wireless Earbuds Pro (stock 8), Cotton Galabeya, Non-stick Frying Pan 28cm
  (stock 3), Sun Cream SPF50 (stock 0), Egyptian Cotton Towel Set, Arabica Coffee 1kg.
  The assignment data table was not present in the repository, so these values were
  chosen to satisfy every worked example and scenario in the brief.
