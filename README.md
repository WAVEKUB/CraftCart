# CraftCart Backend API

## Overview
This is a Spring Boot application providing the backend APIs for the CraftCart e-commerce platform. The system handles users, authentication, products, categories, shopping carts, and orders.

## Configuration
- **Port**: `8080` (Default)
- **Base API Path**: `/api/v1`

## API Endpoints

### 1. Auth (Authentication)
**Base Path:** `/api/v1/auth`

| Method | Endpoint | Description | Request |
|--------|----------|-------------|---------|
| `POST` | `/login` | User login and retrieve JWT | `LoginRequest` (email, password) |

---

### 2. Users
**Base Path:** `/api/v1/users`

| Method | Endpoint | Description | Request |
|--------|----------|-------------|---------|
| `GET` | `/{userId}/user` | Get a user by ID | - |
| `POST` | `/add` | Register/Create a new user | `CreateUserRequest` |
| `PUT` | `/{userId}/update` | Update user details | `UserUpdateRequest` |
| `DELETE` | `/{userId}/delete` | Delete a user | - |

---

### 3. Categories
**Base Path:** `/api/v1/categories`

| Method | Endpoint | Description | Request |
|--------|----------|-------------|---------|
| `GET` | `/all` | Get all categories | - |
| `POST` | `/add` | Add a new category | `Category` JSON |
| `GET` | `/category/{id}/get` | Get category by ID | - |
| `GET` | `/category/{name}/get`| Get category by name | - |
| `PUT` | `/category/{id}/update`| Update a category | `Category` JSON |
| `DELETE` | `/category/{id}/delete`| Delete a category | - |

---

### 4. Products
**Base Path:** `/api/v1/products`

| Method | Endpoint | Description | Request / Params |
|--------|----------|-------------|------------------|
| `GET` | `/all` | Get all products | - |
| `GET` | `/product/{id}/get` | Get product by ID | - |
| `POST` | `/add` | Add a new product (Admin) | `AddProductRequest` |
| `PUT` | `/product/{id}/update` | Update product (Admin) | `ProductUpdateRequest` |
| `DELETE`| `/product/{id}/delete` | Delete product (Admin) | - |
| `GET` | `/all/brand/and-name` | Get products by brand & name | Query: `brandName`, `productName` |
| `GET` | `/all/{category}/get` | Get products by category | Path: `category` |
| `GET` | `/all/{productName}` | Get products by name | Path: `productName` |
| `GET` | `/all/by-brand` | Get products by brand | Query: `brand` |
| `GET` | `/count/by-brand/and-name`| Count products by brand & name | Query: `brand`, `product` |

---

### 5. Images
**Base Path:** `/api/v1/images`

| Method | Endpoint | Description | Request / Params |
|--------|----------|-------------|------------------|
| `POST` | `/upload` | Upload images for a product | `List<MultipartFile>`, Query: `productId` |
| `GET` | `/image/download/{id}`| Download/View image | - |
| `PUT` | `/image/{imageId}/update`| Update image | `MultipartFile` |
| `DELETE` | `/image/{imageId}/delete`| Delete image | - |

---

### 6. Carts
**Base Path:** `/api/v1/carts`

> ⚠️ All cart endpoints require a valid JWT token. The cart is automatically resolved from the authenticated user.

| Method | Endpoint | Description | Request / Params |
|--------|----------|-------------|------------------|
| `GET` | `/my-cart` | Get cart details (authenticated user) | - |
| `GET` | `/total-price` | Get cart total price (authenticated user) | - |
| `DELETE` | `/clear` | Clear all items in cart (authenticated user) | - |

---

### 7. Cart Items
**Base Path:** `/api/v1/cartItems`

| Method | Endpoint | Description | Request / Params |
|--------|----------|-------------|------------------|
| `POST` | `/item/add` | Add item to cart | Query: `productId`, `quantity` |
| `PUT` | `/cart/{cartId}/item/{itemId}/update` | Update item quantity in cart| Query: `quantity` |
| `DELETE`| `/cart/{cartId}/item/{itemId}/remove` | Remove item from cart | - |

---

### 8. Orders
**Base Path:** `/api/v1/orders`

| Method | Endpoint | Description | Request / Params |
|--------|----------|-------------|------------------|
| `POST` | `/order` | Place an order | Query: `userId` |
| `GET` | `/order/{orderId}` | Get order by ID | - |
| `GET` | `/{userId}/orders` | Get all orders of a user | - |

## How to Run

### Prerequisites
- Java 24
- MySQL 9.0 (if running locally)
- Docker & Docker Compose (optional, for containerized execution)

### Method 1: Using Docker Compose (Recommended)
The easiest way to run the application along with the MySQL database is using Docker Compose. The `docker-compose.yml` file handles setting up the database and the backend app.

1. Ensure Docker is running.
2. Run the following command from the project root:
   ```bash
   docker-compose up --build -d
   ```
3. The backend API will be accessible at `http://localhost:8080`.

To stop the application and database, run:
```bash
docker-compose down
```

### Method 2: Running Locally with Maven
To run the application locally without Docker, you will need a running MySQL instance.

1. Start your local MySQL server. Create a database for the application (default name is `craftcart`).
2. The project uses an `.env` file for configuration. Verify or update the `.env` file in the root directory to match your local database credentials:
   ```env
   DB_NAME=craftcart
   DB_USERNAME=root
   DB_PASSWORD=your_password
   JWT_SECRETKEY=your_secret_key
   ```
3. Run the Spring Boot application using the provided Maven wrapper:
   **Windows:**
   ```cmd
   mvnw.cmd spring-boot:run
   ```
   **Mac/Linux:**
   ```bash
   ./mvnw spring-boot:run
   ```
4. The API will be accessible at `http://localhost:8080`.
