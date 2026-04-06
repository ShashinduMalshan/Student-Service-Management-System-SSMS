
---

#  Student Service Management System

##  Project Overview

The **Student Service Management System** is a Spring Boot-based REST API designed to manage student data efficiently. It provides full CRUD operations with a clean layered architecture, following **SOLID principles**, and includes features like DTO mapping, validation, logging, and pagination.

---

##  Architecture Overview

This project follows a **Layered Architecture (N-Tier Architecture)** to ensure scalability, maintainability, and separation of concerns.

```
Controller → Service → Repository → Database
         ↘ DTO ↔ Mapper ↔ Entity
```

###  Package Structure

```
com.example.student_service_management_system
 ├─ controller
 │    └─ StudentController
 ├─ dto
 │    ├─ StudentRequestDTO
 │    └─ StudentResponseDTO
 ├─ entity
 │    └─ Student
 ├─ exception
 │    └─ ResourceNotFoundException
 ├─ repository
 │    └─ StudentRepository
 ├─ service
 │    ├─ StudentService
 │    └─ impl
 │         └─ StudentServiceImpl
 ├─ mapper
 │    └─ StudentMapper
 ├─ config
 │    └─ AppConfig
 └─ StudentServiceManagementSystemApplication
```

---

##  Layer Explanation

### 🔹 Controller Layer

* Handles HTTP requests and responses.
* Uses REST endpoints.
* Delegates business logic to the service layer.

 Example:

* `POST /api/v1/students`
* `GET /api/v1/students`

---

### 🔹 Service Layer

* Contains business logic.
* Communicates with repository and mapper.
* Ensures clean separation from controller.

---

### 🔹 Repository Layer

* Uses Spring Data JPA.
* Handles database operations.

---

### 🔹 Entity Layer

* Represents database table (`Student`).
* Uses JPA annotations.

---

### 🔹 DTO Layer

DTO = Data Transfer Object

* `StudentRequestDTO` → Used for incoming data
* `StudentResponseDTO` → Used for outgoing data

#### Purpose:

* Avoid exposing entity directly
* Improve security and flexibility

---

### 🔹 Mapper Layer

* Converts between **DTO ↔ Entity**
* Uses `ModelMapper`

#### Benefits:

* Keeps service clean
* Centralized conversion logic
* Easy maintenance

---

### 🔹 Config Layer

* Defines Spring Beans
* Example: `ModelMapper` bean

---

### 🔹 Exception Handling

* Custom exception: `ResourceNotFoundException`
* Improves error clarity

---

## Request Flow

```
Client → Controller → Service → Mapper → Repository → Database
                                    ↓
Client ← Controller ← Service ← Mapper ← Entity
```

#### Flow Explanation:

1. Client sends request (JSON)
2. Controller receives request
3. DTO passed to Service
4. Mapper converts DTO → Entity
5. Repository saves data
6. Entity converted back → DTO
7. Response returned to client

---

##  SOLID Principles Used

### 1. Single Responsibility Principle (SRP)

* Each class has one responsibility

    * Controller → Handles requests
    * Service → Business logic
    * Mapper → Conversion

---

### 2. Open/Closed Principle (OCP)

* Classes are open for extension but closed for modification
* Example: You can extend service logic without modifying existing code

---

### 3. Liskov Substitution Principle (LSP)

* Service implementations can replace interfaces without breaking functionality

---

### 4. Interface Segregation Principle (ISP)

* `StudentService` interface is specific and clean

---

### 5. Dependency Inversion Principle (DIP)

* Uses dependency injection (`@RequiredArgsConstructor`)
* Depends on abstractions, not concrete classes

---

##  Logging

#### Integrated **SLF4J with Logback**

### Features:

* Logs key operations:

    * Student creation
    * Updates
    * Deletions
    * Errors
* Uses different levels:

    * `INFO` → Normal operations
    * `ERROR` → Exceptions

#### Example:

```java
logger.info("Adding new student with name: {}", dto.getName());
logger.error("Student not found with ID: {}", id);
```

---

##  Validation

* Implemented using **Jakarta Validation**
* Ensures data integrity

### Annotations Used:

* `@NotBlank`
* `@Size`
* `@Email`

---


## Database Configuration

This project uses **Spring Profiles** to separate production and testing databases.

### MySQL (Production)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/SSMS_System
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Replace `YOUR_DB_USERNAME` and `YOUR_DB_PASSWORD` with your actual credentials.

## Supabase Configuration

Before running the project, create an `application.properties` file with your Supabase credentials:

```properties
# Supabase configuration
supabase.url=YOUR_SUPABASE_URL
supabase.api.key=YOUR_SUPABASE_API_KEY
supabase.bucket=YOUR_BUCKET_NAME
````

### H2 (In-Memory, Testing)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
```

* H2 database is used for fast integration and unit testing.
* Spring Boot automatically switches between MySQL and H2 based on the active profile.

---

This section clearly documents your database setup, shows both MySQL and H2 configurations.

---

##  API Endpoints

| Method | Endpoint                | Description                   |
| ------ | ----------------------- | ----------------------------- |
| POST   | `/api/v1/students`      | Add student                   |
| PUT    | `/api/v1/students/{id}` | Update student                |
| DELETE | `/api/v1/students/{id}` | Delete student                |
| GET    | `/api/v1/students`      | Get all students (pagination) |
| GET    | `/api/v1/students/{id}` | Get student by ID             |

---

## Technologies Used

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* ModelMapper
* Lombok
* SLF4J / Logback
* Postman

---

##  Testing

* Unit Testing → JUnit + Mockito
* Integration Testing → Spring Boot Test

---

##  Postman Collection

* Includes all CRUD APIs
* Organized with proper request names
* Ready for testing and submission

---

##  Author

**Shasidu Malshan Fernando**

---

##  Conclusion

This project demonstrates:

* Clean architecture
* SOLID principles
* Industry-level coding practices
* Scalable and maintainable backend design

---
