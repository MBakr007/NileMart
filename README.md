# NileMart

Console-only Spring Framework project for **NileMart Assignment 1 – Spring Framework
Fundamentals (Sessions 1 & 2)**. The deliverable lives in `nile-mart-core` and uses
plain Spring (no Boot, no web, no database).

## Build and test

All commands are run from `nile-mart-core`:

```bash
mvn clean test          # compiles everything and runs the 51 verification tests
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

## Catalogue (assignment data table)

| SKU | Name | Category | Price (EGP) | Stock |
|-----|------|----------|-------------|-------|
| PH-X5-128 | Smartphone X5 128GB | Electronics | 16,000.00 | 12 |
| ACC-CHG-20W | Fast Charger 20W | Electronics | 650.00 | 40 |
| ACC-BUDS-6 | Wireless Earbuds | Electronics | 1,100.00 | 8 |
| FSH-GAL-001 | Cotton Galabeya | Fashion | 900.00 | 25 |
| HOM-PAN-28 | Non-stick Frying Pan 28cm | Home & Kitchen | 1,200.00 | 3 |
| HOM-SPC-RCK | Wooden Spice Rack | Home & Kitchen | 750.00 | 10 |
| BTY-SUN-50 | Sun Cream SPF50 | Beauty | 350.00 | 0 |
| BK-CAI-TRI | The Cairo Trilogy | Books | 450.00 | 15 |

The console menu is data-driven, so every product and SKU above is available
without any changes to `Main`.

## Expected checkout results

| # | Cart | Governorate | Subtotal | VAT | Shipping | Total |
|---|------|-------------|----------|-----|----------|-------|
| 1 | 2 x ACC-CHG-20W + 1 x FSH-GAL-001 | Giza (standard) | 2,200.00 | 308.00 | 50.00 | 2,558.00 |
| 2 | Same cart, promotional 20% | Giza | 1,760.00 | 246.40 | 50.00 | 2,056.40 |
| 3 | 1 x PH-X5-128 | Aswan | 16,000.00 | 2,240.00 | 0.00 | 18,240.00 |
| 4 | 1 x PH-X5-128 | South Sinai | 16,000.00 | 2,240.00 | 120.00 | 18,360.00 |

## Shipping zones (assignment data table)

| Zone | Fee (EGP) | Free shipping | Governorates |
|------|-----------|---------------|--------------|
| GREATER_CAIRO | 50.00 | Yes | Cairo, Giza, Qalyubia |
| ALEX_DELTA | 65.00 | Yes | Alexandria, Beheira, Kafr El Sheikh, Dakahlia, Damietta, Gharbia, Monufia, Sharqia |
| CANAL | 70.00 | Yes | Port Said, Ismailia, Suez |
| UPPER_EGYPT | 85.00 | Yes | Faiyum, Beni Suef, Minya, Asyut, Sohag, Qena, Luxor, Aswan |
| REMOTE | 120.00 | No | Matrouh, North Sinai, South Sinai, Red Sea, New Valley |

Shipping is free only when `subtotal >= 5000.00` **and** the zone is eligible.
A subtotal of exactly `5000.00` qualifies.

## Implementation notes

- Money is always `BigDecimal` with scale 2 and `RoundingMode.HALF_UP`.
- Business-rule failures throw `StoreException`.
- All dependencies use constructor injection; `ShoppingCart` is a prototype bean.
- `ExecutionLog` times every `*Service` method; `AuditLog` logs checkout attempts.
- The catalogue and governorate tables are locked by `CatalogueSpecificationTest`
  and the parameterized `ShippingAndWiringTest`.
