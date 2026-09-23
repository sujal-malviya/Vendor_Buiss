# VendorHub — Catering Marketplace & Operations Platform

A two-sided platform connecting caterers/vendors with customers/event hosts —
covering vendor onboarding, menu & package management, booking & quotation,
order versioning, payments, event operations, resource management, inventory,
and a vendor dashboard.

Full product scope is documented in `Catering_Platform_BRD_v2.docx`
(Business & Product Requirements Document — consolidated build, all three
original delivery phases combined into one).

## Tech Stack

- **Java 21**
- **Spring Boot 3.3.4**
- **Gradle**
- **PostgreSQL**
- **Spring Security** + **JWT** (jjwt) — stateless authentication
- **Lombok**

## Status

### ✅ Implemented
- Vendor registration and login (`/api/auth/register`, `/api/auth/login`)
- JWT-based stateless authentication (issue, validate, filter chain)
- Password hashing with BCrypt
- Global exception handling for validation and bad-request errors
- `Vendor` entity (base auth fields: id, email, password, role, onboardingComplete)

### 🚧 Planned (see BRD Section 22 — Consolidated Build Scope)
Organized by domain, to be built and tested one at a time:

| Domain | Core Entities |
|---|---|
| Identity & Profile | Customer, Dish, Package, VendorPolicy, VendorCapacity |
| Lead Generation & Sharing | MenuLink (public/private) |
| Booking, Quotation & Versioning | Order, OrderVersion, OrderItem, Quotation |
| Payments | Payment, PaymentLink, PlatformFee |
| Event Operations | Event, EventChecklist, EventDayLog, EventClosure |
| Resource Management | Staff, Equipment, RawMaterial, Logistics |
| Financial & Inventory | Expense, FinancialSummary, InventoryItem |
| Notifications & Dashboard | Notification, dashboard aggregation views |

## Project Structure

```
com.vendorhub
├── VendorOnboardingApplication.java
├── config/          → SecurityConfig, JwtAuthFilter
├── controller/      → AuthController (+ upcoming domain controllers)
├── service/         → AuthService, CustomUserDetailsService
├── repository/      → VendorRepository
├── entity/          → Vendor
├── dto/             → RegisterRequest, LoginRequest, AuthResponse
├── security/        → JwtUtil
└── exception/       → GlobalExceptionHandler
```

## Setup

1. Create the database:
   ```sql
   CREATE DATABASE vendor_onboarding;
   ```

2. Update `src/main/resources/application.properties` with your local
   Postgres username/password.

3. **Important:** replace `jwt.secret` with your own long random string
   before running anything beyond local testing.

4. Run the app:
   ```bash
   ./gradlew bootRun
   ```

## API Reference

### Auth (live)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create a vendor account, returns a JWT |
| POST | `/api/auth/login` | Authenticate, returns a JWT |

All routes other than `/api/auth/**` require `Authorization: Bearer <token>`.

### Vendor Onboarding (planned — POST, insert-or-update per section)
| Endpoint | Section |
|---|---|
| `/api/vendor/business-info` | Business identity, GSTIN, FSSAI |
| `/api/vendor/bank-details` | Payout details |
| `/api/vendor/service-area` | Capacity, PIN/city, min/max order |
| `/api/vendor/order-policies` | Edit window, cancellation, max discount |
| `/api/vendor/payment-plan` | Advance/pre/post payment % |
| `/api/vendor/media` | Photos, videos |
| `/api/vendor/required-items` | Table, chair, light, generator |
| `/api/vendor/event-types` | Supported event types |
| `/api/vendor/dishes` | Add dish |
| `/api/vendor/packages` | Add package |
| `/api/vendor/onboarding/status` | Section completion status |
| `/api/vendor/onboarding` | Full onboarding read (resume/review) |
| `/api/vendor/onboarding/submit` | Mark onboarding complete |

## Testing (Postman / curl)

**Register**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "vendor1@test.com",
  "password": "password123"
}
```

**Login**
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "vendor1@test.com",
  "password": "password123"
}
```

## Documentation

Full requirements, business model canvas, process diagrams, data-model
domains, and settled product/policy decisions live in
`Catering_Platform_BRD_v2.docx`.
