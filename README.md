# CraftCart

Simple E-Commerce system built with Java and Maven.

> 🚧 This project is a work in progress. Contributions and feedback are welcome!

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running the App](#running-the-app)
- [API Overview](#api-overview)
- [Testing](#testing)
- [Common Tasks](#common-tasks)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- Product catalog (list/search products)
- Shopping cart (add/update/remove items)
- Orders & checkout flow (draft → placed → paid)
- Basic authentication/authorization (placeholder)
- Environment-based configuration

---

## Tech Stack

- **Language:** Java (Maven wrapper included)
- **Framework:** Spring Boot (Web, Validation, …)
- **Data:** Spring Data JPA with H2/MySQL (choose one)
- **Build:** Maven (`mvnw` / `mvnw.cmd`)
- **Testing:** JUnit + Spring Boot Test

---

## Project Structure

```
CraftCart/
├─ .mvn/                # Maven wrapper
├─ src/
│  ├─ main/
│  │  ├─ java/...       # Application source (controllers, services, repos, models)
│  │  └─ resources/
│  │     ├─ application.yml (or application.properties)
│  │     └─ static/templates (if using MVC views)
│  └─ test/             # Unit & integration tests
├─ pom.xml              # Dependencies & build config
├─ mvnw, mvnw.cmd       # Maven wrapper scripts
└─ README.md
```

---

## Getting Started

### Prerequisites

- **Java:** 17+ (LTS recommended)  
- **Maven:** use the included wrapper (`./mvnw`) — no global install required

### Clone

```bash
git clone https://github.com/WAVEKUB/CraftCart.git
cd CraftCart
```

---

## Configuration

Spring Boot reads configuration from `application.yml` or `application.properties`.

### Example (H2 in-memory)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:craftcart;DB_CLOSE_DELAY=-1;MODE=MySQL
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  h2:
    console:
      enabled: true
```

### Example (MySQL)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/craftcart?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: craftcart
    password: change-me
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate.dialect: org.hibernate.dialect.MySQL8Dialect
```

---

## Running the App

### With Maven wrapper

```bash
# clean & run
./mvnw spring-boot:run

# or build a jar
./mvnw clean package
java -jar target/*.jar
```

The app will start on `http://localhost:8080` by default.

---

## API Overview

### Products

- `GET /api/products` – list products (query: `q`, `page`, `size`)
- `GET /api/products/{id}` – product detail
- `POST /api/products` – create (admin)
- `PUT /api/products/{id}` – update (admin)
- `DELETE /api/products/{id}` – delete (admin)

### Cart

- `GET /api/cart` – get current cart
- `POST /api/cart/items` – add item `{ productId, quantity }`
- `PATCH /api/cart/items/{itemId}` – update quantity
- `DELETE /api/cart/items/{itemId}` – remove item
- `DELETE /api/cart` – clear cart

### Orders

- `POST /api/orders` – place order from current cart
- `GET /api/orders/{id}` – order detail
- `GET /api/orders` – list my orders

---

## Testing

```bash
./mvnw test
```

---

## Common Tasks

- **Format & Lint:** Add `spotless` + `checkstyle` plugins (optional)
- **Seed Data:** Use `data.sql` or `CommandLineRunner` for sample products
- **Profiles:** `application-local.yml`, `application-dev.yml`, `application-prod.yml`
- **Docker (optional):** Add a `Dockerfile` and `docker-compose.yml` for app + DB

---

## Troubleshooting

- **Port busy (8080):** change `server.port` in config or free the port
- **DB connection errors:** verify JDBC URL/credentials; ensure DB is running
- **CORS issues:** configure `CorsRegistry` or Spring Security CORS mappings
- **Build fails:** run `./mvnw -v` and use Java 17+; delete `~/.m2/repository` cache if needed

---

## Roadmap

- [ ] User registration & JWT login
- [ ] Payment gateway integration (sandbox)
- [ ] Admin dashboard (products, orders, inventory)
- [ ] Pagination, sorting, filtering
- [ ] Observability (Spring Boot Actuator, logs, metrics)

---

## Contributing

1. Fork the repo and create a feature branch:
   ```bash
   git checkout -b feature/your-feature
   ```
2. Commit changes with clear messages and tests.
3. Open a Pull Request with a summary of changes.

---

## License

Add your preferred license (e.g., MIT) in `LICENSE` and reference it here.
