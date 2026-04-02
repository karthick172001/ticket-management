# IT Support Ticket Management System

A production-ready RESTful backend application built with **Spring Boot** to manage the complete IT support ticket lifecycle — from creation to resolution — with JWT-based authentication and Role-Based Access Control (RBAC).

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL |
| Build Tool | Maven |

## Features

- JWT Authentication with stateless session management
- Role-Based Access Control (RBAC) — Admin, Agent, User
- Full ticket lifecycle: Create → Assign → In Progress → Resolve → Close
- Comment system per ticket
- Global exception handling with clean JSON error responses
- Request validation using Jakarta Validation

## Roles & Permissions

| Action | USER | AGENT | ADMIN |
|--------|------|-------|-------|
| Register/Login | ✅ | ✅ | ✅ |
| Create ticket | ✅ | ✅ | ✅ |
| View own tickets | ✅ | — | — |
| View assigned tickets | — | ✅ | — |
| View all tickets | — | — | ✅ |
| Add comment | ✅ (own) | ✅ | ✅ |
| Assign ticket | ❌ | ❌ | ✅ |
| Resolve ticket | ❌ | ✅ | ✅ |
| Close ticket | ❌ | ❌ | ✅ |

## Project Structure

```
src/main/java/com/karthick/ticketmgmt/
├── config/          # Security configuration
├── controller/      # REST API controllers
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── enums/           # Role, TicketStatus, TicketPriority
├── exception/       # Custom exceptions + global handler
├── repository/      # Spring Data JPA repositories
├── security/        # JWT utility + filter
└── service/         # Business logic
```

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+

### Setup

1. Clone the repository
```bash
git clone https://github.com/karthick172001/ticket-management.git
cd ticket-management
```

2. Create MySQL database
```sql
CREATE DATABASE ticketdb;
```

3. Update `src/main/resources/application.properties`
```properties
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

4. Run the application
```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Tickets
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/tickets` | Create ticket | All |
| GET | `/api/tickets` | Get tickets (role-filtered) | All |
| GET | `/api/tickets/{id}` | Get ticket by ID | All |
| PUT | `/api/tickets/{id}` | Update ticket | All |
| PATCH | `/api/tickets/{id}/assign` | Assign to agent | Admin |
| PATCH | `/api/tickets/{id}/resolve` | Resolve ticket | Agent, Admin |
| PATCH | `/api/tickets/{id}/close` | Close ticket | Admin |
| GET | `/api/tickets/status/{status}` | Filter by status | Admin |

### Comments
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tickets/{id}/comments` | Add comment |
| GET | `/api/tickets/{id}/comments` | Get comments |

## Sample Requests

### Register
```json
POST /api/auth/register
{
  "name": "Karthikeyan",
  "email": "admin@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "admin@example.com",
  "password": "password123"
}
```
Response includes JWT token — use as `Authorization: Bearer <token>` in all subsequent requests.

### Create Ticket
```json
POST /api/tickets
Authorization: Bearer <token>
{
  "title": "Laptop not booting",
  "description": "My laptop shows a black screen on startup",
  "priority": "HIGH"
}
```

## Author

**Karthikeyan G** — Java Backend Developer  
[LinkedIn](https://linkedin.com/in/karthikeyan172001) | [GitHub](https://github.com/karthick172001)
