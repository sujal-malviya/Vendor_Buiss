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
- **Flyway** — versioned database migrations (`src/main/resources/db/migration`)
- **Spring Security** + **OAuth2 Resource Server** (Nimbus JWT, HS256) — stateless authentication
- **springdoc-openapi** — Swagger UI at `/swagger-ui.html`
- **Lombok**

## Status

### ✅ Implemented
- Vendor registration and login (`/api/auth/register`, `/api/auth/login`)
- Customer registration and login (`/api/customer/auth/register`, `/api/customer/auth/login`)
- JWT-based stateless authentication; the token's `scope` claim is `VENDOR`, `ADMIN` or `CUSTOMER`,
  and a customer token can never reach `/api/vendor/**` (or the other way round)
- Password hashing with BCrypt
- Vendor onboarding sections (profile, business info, bank details, service area,
  order policies, payment plan, media) — each vendor can only see and change their own
- Menu: packages, dishes inside packages, and the vendor's own dish prices
- Shared lists (dish catalog, event types, required items) — readable by everyone, changeable only by admins
- Request/response DTOs for every endpoint (entities never leave the service layer)
- Pagination on lists that can grow (`?page=0&size=20&sort=name,asc`, max size 100)
- Global exception handling: 400 validation errors, 404 not found, 409 conflicts
- Integration tests (`VendorApiTests`, `CustomerApiTests`)

### 🚧 Planned (see BRD Section 22 — Consolidated Build Scope)
Organized by domain, to be built and tested one at a time:

| Domain | Core Entities |
|---|---|
| Identity & Profile | Customer profile & addresses, VendorCapacity |
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
├── config/       → SecurityConfig (JWT encoder/decoder, access rules), OpenApiConfig (Swagger)
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

   On startup Flyway creates or updates the tables, then Hibernate checks the entities match them.

4. Open Swagger UI to explore and try the API: http://localhost:8080/swagger-ui.html
   Log in, copy the token, click **Authorize**, paste it.

5. Run the tests (uses an in-memory database, never touches PostgreSQL):
   ```bash
   ./gradlew test
   ```

### Changing the database structure (Flyway)
Never change tables by hand and never edit an already-applied migration. Instead:
1. Change the entity.
2. Add a new file `src/main/resources/db/migration/V3__short_description.sql` (next free number) with the SQL.
3. Start the app — Flyway applies it once and records it in the `flyway_schema_history` table.

If the entity and the tables don't match, the app refuses to start (`ddl-auto=validate`) and tells you which column is wrong.

### Making an admin
Every new account is a `VENDOR`. To make one an admin, update it in the database and log in again
(the role is read when the token is created):
```sql
UPDATE vendors SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

## API Reference

All routes other than the register/login ones require `Authorization: Bearer <token>`.
Errors are returned as JSON (`status`, `detail`, and for validation errors an `errors` map of field → message).

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create a vendor account, returns a JWT (201) |
| POST | `/api/auth/login` | Vendor login (`identifier` = email or phone), returns a JWT |
| GET | `/api/vendor/me` | Shows who the vendor token belongs to |
| POST | `/api/customer/auth/register` | Create a customer account (`name`, `email`, `phoneNumber`, `password`), returns a JWT (201) |
| POST | `/api/customer/auth/login` | Customer login (`identifier` = email or phone), returns a JWT |
| GET | `/api/customer/me` | The logged-in customer's details (customer token only) |
| GET | `/api/customer/vendors?city=Pune&page=0&size=20` | Customers search vendors (city optional, ignores case). Only vendors with business info **and** a service area are listed. |

### Lists and pagination
`/api/dishes`, `/api/vendor/event-types`, `/api/vendor/required-items`, `/api/vendor/packages`,
`/api/vendor/package-dishes` and `/api/vendor/dishes` return one page at a time:
```
GET /api/vendor/packages?page=0&size=20&sort=name,asc
→ { "content": [ ... ], "page": 0, "size": 20, "totalElements": 57, "totalPages": 3 }
```

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
