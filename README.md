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
- **Spring Boot 4.1.1**
- **Gradle**
- **PostgreSQL** (H2 in-memory database for tests)
- **Spring Security** + **OAuth2 Resource Server** (Nimbus JWT, HS256) — stateless authentication
- **Lombok**

## Status

### ✅ Implemented
- Vendor registration and login (`/api/auth/register`, `/api/auth/login`)
- JWT-based stateless authentication; roles `VENDOR` and `ADMIN` (carried in the token's `scope` claim)
- Password hashing with BCrypt
- Vendor onboarding sections (profile, business info, bank details, service area,
  order policies, payment plan, media) — each vendor can only see and change their own
- Menu: packages, dishes inside packages, and the vendor's own dish prices
- Shared lists (dish catalog, event types, required items) — readable by everyone, changeable only by admins
- Global exception handling: 400 validation errors, 404 not found, 409 conflicts
- Integration tests (`VendorApiTests`)

### 🚧 Planned (see BRD Section 22 — Consolidated Build Scope)
Organized by domain, to be built and tested one at a time:

| Domain | Core Entities |
|---|---|
| Identity & Profile | Customer, VendorCapacity |
| Lead Generation & Sharing | MenuLink (public/private) |
| Booking, Quotation & Versioning | Order, OrderVersion, OrderItem, Quotation |
| Payments | Payment, PaymentLink, PlatformFee |
| Event Operations | Event, EventChecklist, EventDayLog, EventClosure |
| Resource Management | Staff, Equipment, RawMaterial, Logistics |
| Financial & Inventory | Expense, FinancialSummary, InventoryItem |
| Notifications & Dashboard | Notification, dashboard aggregation views |
| Onboarding flow | `/api/vendor/onboarding/status`, `/api/vendor/onboarding`, `/api/vendor/onboarding/submit` |

## Project Structure

```
com.vendorhub.vendor_onboarding
├── VendorOnboardingApplication.java
├── config/       → SecurityConfig (JWT encoder/decoder, access rules)
├── controller/   → REST endpoints (AuthController, VendorController, ...) — only speak DTOs
├── dto/          → XxxRequest (what clients send) and XxxResponse (what clients get back)
├── service/      → business logic, JwtService — converts DTO ⇄ entity
├── repository/   → Spring Data JPA repositories
├── entity/       → JPA entities (Vendor, VendorProfile, ...)
├── security/     → CurrentVendor (who is logged in)
└── exception/    → GlobalExceptionHandler, ResourceNotFoundException
```

## Setup

1. Create the database:
   ```sql
   CREATE DATABASE vendor_onboarding;
   ```

2. Set your settings as environment variables (the values in
   `application.properties` are only local-development defaults):

   | Variable | Default |
   |---|---|
   | `DB_URL` | `jdbc:postgresql://localhost:5432/vendor_onboarding` |
   | `DB_USERNAME` | `postgres` |
   | `DB_PASSWORD` | `postgres` |
   | `JWT_SECRET` | a dev-only value — **always set your own long random string outside local testing** |

3. Run the app:
   ```bash
   ./gradlew bootRun
   ```

4. Run the tests (uses an in-memory database, never touches PostgreSQL):
   ```bash
   ./gradlew test
   ```

### Making an admin
Every new account is a `VENDOR`. To make one an admin, update it in the database and log in again
(the role is read when the token is created):
```sql
UPDATE vendors SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

## API Reference

All routes other than `/api/auth/**` require `Authorization: Bearer <token>`.
Errors are returned as JSON (`status`, `detail`, and for validation errors an `errors` map of field → message).

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create a vendor account, returns a JWT (201) |
| POST | `/api/auth/login` | Authenticate, returns a JWT |
| GET | `/api/vendor/me` | Shows who the token belongs to |

### Vendor onboarding (each vendor sees only their own data)
Create your profile first — the other sections attach to it automatically.
Each section supports `POST` (create, once), `GET` (list yours), `GET /{id}`, `PUT /{id}` (replace),
`PATCH /{id}` (change only the fields you send) and `DELETE /{id}`.

| Endpoint | Section |
|---|---|
| `/api/vendor/profile` | Name and address (deleting it also deletes all sections below) |
| `/api/vendor/business-info` | Business name, contact, GST, FSSAI, years in business |
| `/api/vendor/bank-details` | Payout details |
| `/api/vendor/service-area` | Capacity, PIN/city, min/max order |
| `/api/vendor/order-policies` | Modify window, cancellation grace period |
| `/api/vendor/payment-plan` | Advance/pre/post payment |
| `/api/vendor/media` | Photo and video |
| `/api/vendor/packages` | Your menu packages |
| `/api/vendor/package-dishes` | Dishes inside your packages |
| `/api/vendor/dishes` | Dishes you offer, with your price |

### Shared lists (everyone can read, only `ADMIN` can create/change/delete)
| Endpoint | List |
|---|---|
| `/api/dishes` | Dish catalog |
| `/api/vendor/event-types` | Event types |
| `/api/vendor/required-items` | Table, chair, light, generator, ... |

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

**Login** (`identifier` is your email or phone)
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "identifier": "vendor1@test.com",
  "password": "password123"
}
```

**Create your profile**
```
POST http://localhost:8080/api/vendor/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Spice Caterers",
  "address": "MG Road, Bangalore"
}
```

**Add bank details**
```
POST http://localhost:8080/api/vendor/bank-details
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountNumber": "1234567890",
  "accountHolderName": "Spice Caterers",
  "ifscCode": "HDFC0001234"
}
```
Response — the account number is never sent back in full:
```json
{ "id": 1, "accountHolderName": "Spice Caterers", "maskedAccountNumber": "XXXXXX7890", "ifscCode": "HDFC0001234" }
```

**Put a dish in a package** (send plain ids)
```
POST http://localhost:8080/api/vendor/package-dishes
Authorization: Bearer <token>
Content-Type: application/json

{ "packageId": 3, "dishId": 5, "quantity": 2 }
```

**Offer a catalog dish at your own price**
```
POST http://localhost:8080/api/vendor/dishes
Authorization: Bearer <token>
Content-Type: application/json

{ "dishId": 5, "price": 180, "available": true }
```

**Demo data:** `scripts/demo-data.sql` adds 6 demo vendors (password `password123`) with data in every table.

## Documentation

Full requirements, business model canvas, process diagrams, data-model
domains, and settled product/policy decisions live in
`Catering_Platform_BRD_v2.docx`.
