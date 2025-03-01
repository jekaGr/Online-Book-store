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
- **Spring Boot**: Core framework for building the application
- **Spring Data JPA**: For interacting with the database via Java Persistence API
- **Spring Security**: Ensures secure access and authentication mechanisms
- **Spring MVC**: To handle HTTP requests and responses

### Database & Persistence
- **MySQL**: Relational database for storing application data
- **Liquibase**: For managing database schema changes

### Security & Authentication
- **JWT**: Used for JSON Web Token-based user authentication

### Utility Libraries
- **MapStruct**: For efficient object mapping
- **Lombok**: To reduce boilerplate code

### Documentation & Testing
- **Swagger UI**: To explore the API and view documentation
- **JUnit & Testcontainers**: For unit testing and integration testing

### Build & Deployment
- **Maven**: For managing project dependencies and building the project
- **Docker**: (Optional) For containerizing the application and database

---

## 🚀 Setup and Run Instructions

### Prerequisites
Before you begin, make sure the following software is installed:
- **Java 21** (or later)
- **IntelliJ IDEA** (or another Java IDE)
- **MySQL** (or use Docker for a containerized MySQL setup)
- **Maven** for dependency management
- **Docker** (optional, if you prefer using Docker for database setup)

### 1. Clone the Repository
Clone the project repository to your local machine:
```bash
git clone https://github.com/your-username/bookstore-app.git
cd bookstore-app
```

### 2. Configure the Database
- Use MySQL Workbench or any SQL tool to create a database:
```sql
CREATE DATABASE bookstore;
```
- Open the `application.properties` file (located in `src/main/resources/`) and update it with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build and Run the Application

#### Option 1: Running with Maven
To build and run the application using Maven, execute the following commands:
```bash
mvn clean install
mvn spring-boot:run
```
The app will be accessible at [http://localhost:8080](http://localhost:8080).

#### Option 2: Running with Docker (Optional)
If you'd like to run the app using Docker, you can start the database container with Docker Compose:
```bash
docker-compose up
```
This will spin up the database in a Docker container. The application will then be accessible at [http://localhost:8088](http://localhost:8088).

---

## 📑 API Documentation

For detailed information about the available APIs, visit the Swagger UI at:
[Swagger UI](http://localhost:8080/swagger-ui.html)

---

## 📝 Contributing

Feel free to fork this repository, create issues, or submit pull requests. Contributions are always welcome!

---

## 📄 License

This project is licensed under the MIT License – see the [LICENSE.md](LICENSE.md) file for details.
