# Book Store Web Application

Welcome to the Book Store Web Application! This is a Java-based platform built with Spring Boot, designed to manage a catalog of books and facilitate seamless online purchases. The app utilizes a service-oriented architecture and is developed following SOLID principles, making it scalable and maintainable.

Key features include secure user authentication, a RESTful API for all operations, and support for CRUD functionalities. The app also provides advanced search capabilities, pagination, sorting, and soft deletes. With Swagger integrated for API documentation and Liquibase for database version control, this application ensures robust error handling and thorough data validation.

---

## 🛠️ Key Features

### 📚 Book Catalog Management
- Fetch all books or filter by title, author, or category
- View book details using the unique ID
- Admin users can add, edit, or remove books

### 👤 User Authentication & Roles
- Users can register and log in securely via JWT tokens
- Role-based access control with distinct permissions for Admins and Regular Users

### 🛒 Shopping Cart & Order Processing
- Users can manage their shopping cart: add, update, or remove items
- Place and track orders, including order history management
- Admins can update order statuses (e.g., processing, shipped)

### 📂 Category Management
- Users can browse categories and view books within each category
- Admins have the ability to add, edit, or delete categories

### 🔧 Advanced Features
- Pagination and sorting mechanisms for efficient data retrieval
- Global exception handling for a smoother user experience
- Swagger UI for API documentation and easy interaction with the backend

---

## 🏗️ Project Architecture

The application is structured around a three-layer architecture:

1. **Controller Layer**: Handles incoming HTTP requests and generates responses.
2. **Service Layer**: Contains the business logic and communicates with the repository layer.
3. **Repository Layer**: Responsible for interactions with the database using JPA.

### Core Entities:
- **User**: Contains user profile details and authentication information.
- **Role**: Specifies user roles (e.g., USER, ADMIN).
- **Book**: Represents a book in the catalog.
- **Category**: Organizes books into categories.
- **ShoppingCart**: Manages the items that the user intends to purchase.
- **CartItem**: Represents a single item within the shopping cart.
- **Order**: Contains the details of a placed order.
- **OrderItem**: Represents the books that are part of an order.

---

## 🔧 Technologies Used

### Frameworks & Libraries
- **Spring Boot 3.4.1**: Core framework for building the application
- **Spring Data JPA 3.4.1**: For interacting with the database via Java Persistence API
- **Spring Security 6.4.2**: Ensures secure access and authentication mechanisms
- **Spring MVC 6.2.1**: To handle HTTP requests and responses

### Database & Persistence
- **MySQL 8.2.0**: Relational database for storing application data
- **Liquibase 4.29.2**: For managing database schema changes

### Security & Authentication
- **JWT 0.12.6**: Used for JSON Web Token-based user authentication

### Utility Libraries
- **MapStruct 1.6.3**: For efficient object mapping
- **Lombok 1.18.36**: To reduce boilerplate code

### Documentation & Testing
- **Swagger UI 4.18.2**: To explore the API and view documentation
- **JUnit 5.11.4 & Testcontainers 1.20.5**: For unit testing and integration testing

### Build & Deployment
- **Maven 3.8.4**: For managing project dependencies and building the project
- **Docker 20.10.12**: (Optional) For containerizing the application

---

## 🌐 API Endpoints and Access Levels

Here's a list of available endpoints along with the access levels required:

### Authentication:
- **POST /auth/register**: Register a new user (Public)
- **POST /auth/login**: Authenticate a user and generate a JWT token (Public)

### Book Management:
- **GET /books**: Fetch all books (User)
- **GET /books/{id}**: Fetch a specific book by ID (User)
- **GET books/search**: Search for books based on specific parameters (User)
- **POST /api/books**: Add a new book (Admin)
- **PUT /api/books/{id}**: Update book details (Admin)
- **DELETE /api/books/{id}**: Soft delete a book (Admin)

### Category Management:
- **GET /categories**: Fetch all categories (Public)
- **GET /categories/{id}**: Fetch a specific category by ID (Public)
- **POST /categories**: Add a new category (Admin)
- **PUT /categories/{id}**: Update category details (Admin)
- **DELETE /categories/{id}**: Soft delete a category (Admin)
- **GET /categories/{id}/books**: Get books by category ID (Public)

### Shopping Cart:
- **GET /cart**: Fetch user's shopping cart (User)
- **POST /cart**: Add an item to the cart (User)
- **PUT /cart/items/{cartItemId}**: Update an item in the cart (User)
- **DELETE /cart/items/{cartItemId}**: Remove an item from the cart (User)

### Order Management:
- **GET /orders**: Fetch all orders of the authenticated user (User)
- **GET /orders/{orderId}/items**: Get items of a specific order (User)
- **POST /orders**: Place a new order (User)
- **PATCH orders/{orderId}**: Update order status (Admin)
- **GET /orders/{orderId}/items/{itemId}**: Get details of a specific item in an order (User)
---

## 🛠️ Working with the .env File

To manage sensitive information and configuration settings, create a `.env` file in the root directory of your project. This file will contain environment variables that will be used by the application.

### Example .env File
```env
MYSQLDB_USER=YOUR_USERNAME
MYSQL_ROOT_PASSWORD=YOUR_PASSWORD
MYSQL_DATABASE=bookstore
MYSQLDB_LOCAL_PORT=YOUR_LOCAL_PORT(3307)
MYSQLDB_DOCKER_PORT=MYSQLLOCAL_PORT(3306)

SPRING_LOCAL_PORT=YOUR_HOST_LOCAL_PORT(8081)
SPRING_DOCKER_PORT=HOST_LOCAL_PORT(8080)
DEBUG_PORT=5005