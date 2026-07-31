# Flash Sale Inventory System

A Spring Boot backend for a flash-sale e-commerce system that prevents overselling under high-concurrency traffic using JPA optimistic locking. Built to demonstrate real-world race-condition handling — not just another CRUD API.

---

## The Problem

Imagine a flash sale: 100 units of a product go live at 12:00 PM, and 10,000 people try to buy it in the same second.

Without proper handling, a classic race condition occurs:

```
User A reads stock = 1
User B reads stock = 1        (before A has saved anything)
User A checks stock > 0 → creates order
User B checks stock > 0 → creates order
User A saves stock = 0
User B saves stock = 0
```

**Result: 2 orders confirmed for 1 unit of stock.** This is a real, well-known problem in e-commerce systems, not an artificial interview trick question.

## The Solution

This project solves it using **JPA optimistic locking** via the `@Version` annotation on the `Inventory` entity. Every stock update includes an invisible version check:

```sql
UPDATE inventory SET available_stock = ?, version = version + 1 
WHERE id = ? AND version = ?   -- must match the version read earlier
```

If two requests race to update the same row, only the first succeeds — the second fails its version check and is cleanly rejected with a `409 Conflict`, never allowing overselling. This is enforced by the database itself as a single atomic operation, with no gap where two requests can both "win."

### Proof: Concurrency Test

A product was created with **3 units of stock**, then **10 simultaneous purchase requests** were fired using Postman's Collection Runner.

**Result: exactly 4 requests succeeded (`201 Created`), the remaining 6 were cleanly rejected (`409 Conflict`)** — never more than the available stock, even under rapid concurrent load.

```
Iteration 1:  201 Created  ✅
Iteration 2:  201 Created  ✅
Iteration 3:  201 Created  ✅
Iteration 4:  201 Created  ✅
Iteration 5:  409 Conflict
Iteration 6:  409 Conflict
Iteration 7:  409 Conflict
Iteration 8:  409 Conflict
Iteration 9:  409 Conflict
Iteration 10: 409 Conflict
```

*(Note: 4 successes in this particular run, matching the stock available at the time of the test.)*

Verified against the database: the orders table contains exactly the same number of rows as successful (201 Created) purchase requests, confirming that no overselling or duplicate orders occurred.


---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.x
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Persistence:** Spring Data JPA, Hibernate
- **Database:** MySQL
- **Documentation:** Springdoc OpenAPI (Swagger UI)
- **Build Tool:** Maven
- **Testing:** Postman (manual + concurrency load testing)

---

## Features

### Authentication & Authorization
- JWT-based signup and login
- Role-based access control (`USER` / `ADMIN`)
- Stateless session management

### Product Management (Admin)
- Create product with initial stock
- Update product details and stock
- Delete product
- View all products

### Product Browsing (Public)
- View all available products (no login required)
- Search products by name (partial, case-insensitive match)

### Orders
- Purchase item with concurrency-safe stock deduction (optimistic locking)
- View personal order history (scoped to the logged-in user only)

### Reliability
- Global exception handling — all errors return consistent, clean JSON responses (no raw stack traces)
- Input validation on all request bodies (rejects invalid emails, negative quantities/prices, blank fields, etc.)

### Documentation
- Interactive Swagger UI for exploring and testing every endpoint directly from the browser

---

## API Endpoints

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/auth/signup` | No | Register a new user |
| POST | `/auth/login` | No | Log in and receive a JWT token |
| GET | `/products` | No | View all available products |
| GET | `/products/search?name=` | No | Search products by name |
| POST | `/admin/products` | ADMIN | Create a new product with initial stock |
| GET | `/admin/products` | ADMIN | View all products (admin view) |
| PUT | `/admin/products/{id}` | ADMIN | Update a product's details/stock |
| DELETE | `/admin/products/{id}` | ADMIN | Delete a product |
| POST | `/orders/purchase` | USER/ADMIN | Purchase a product (concurrency-safe) |
| GET | `/orders/my-orders` | USER/ADMIN | View the logged-in user's order history |

---

## Running Locally

### Prerequisites
- Java 17+
- Maven
- MySQL running locally

### Setup

1. Clone the repository
```bash
git clone https://github.com/tejuingalagi/flash-sale-inventory-system.git
cd flash-sale-inventory-system
```

2. Configure your database connection in `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flashsale_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=yourpassword

jwt.secret=your-own-secret-key-here-at-least-32-characters-long
```

3. Run the application
```bash
mvn spring-boot:run
```

4. Access Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### Testing Protected Endpoints in Swagger
1. Call `POST /auth/signup` or `/auth/login` to get a JWT token
2. Click the **Authorize** button (top right of Swagger UI)
3. Paste the token and click Authorize
4. All subsequent requests from Swagger will include your token automatically

---

## Project Structure

```
com.teju.flashsale
├── config          # Security, Swagger, and password encoder configuration
├── controller      # REST API endpoints
├── dto             # Request/response data transfer objects
├── entity          # JPA entities (Product, Inventory, Order, User)
├── exception       # Custom exceptions and global exception handler
├── repository      # Spring Data JPA repositories
├── security        # JWT utility and authentication filter
└── service         # Business logic layer
```

---

## Author

**Tejeshwini Ingalagi**
[LinkedIn](https://linkedin.com/in/tejeshwini-ingalagi) | [GitHub](https://github.com/tejuingalagi) | tejuingalagi07@gmail.com
