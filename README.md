# Task Manager API

A production-grade RESTful Task Management backend built with Java 21, Spring Boot 3.5, and PostgreSQL.

## Features

- **Authentication:** Stateless JWT-based authentication with access and refresh tokens
- **User Management:** Registration, login, profile retrieval
- **Task CRUD:** Create, read, update, and delete tasks with ownership enforcement
- **Search & Filter:** Case-insensitive text search, filter by status/priority/category
- **Pagination & Sorting:** Configurable page size and sort direction on all list endpoints
- **Role-Based Access Control:** `ROLE_USER` and `ROLE_ADMIN` with method-level security
- **API Documentation:** Swagger UI with JWT authentication support

## Tech Stack

| Layer | Technology |
|:---|:---|
| Language | Java 21 (Amazon Corretto) |
| Framework | Spring Boot 3.5.x |
| Build Tool | Gradle (Groovy DSL) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| Security | Spring Security 6, JWT (JJWT), BCrypt |
| Mapping | MapStruct |
| Validation | Jakarta Bean Validation |
| Documentation | OpenAPI 3.0 / Swagger UI |
| Testing | JUnit 5, Mockito |

## Architecture

```
Controller → Service → Repository → PostgreSQL
     ↓           ↓
   DTOs       Mappers
```

Layered architecture following SOLID principles. Controllers contain no business logic. Services coordinate repositories and mappers. Entities are never exposed through REST endpoints.

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL 16+
- Gradle 8.x (wrapper included)

### Database Setup

```sql
CREATE DATABASE task_manager_db;
```

### Configuration

Copy `.env.example` to `.env` and set your values:

```
DB_URL=jdbc:postgresql://localhost:5432/task_manager_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_base64_encoded_secret
```

Or set environment variables directly.

### Build & Run

```bash
./gradlew build
./gradlew bootRun
```

The API starts at `http://localhost:8080/api/v1`.

### API Documentation

Swagger UI: [http://localhost:8080/api/v1/swagger-ui/index.html](http://localhost:8080/api/v1/swagger-ui/index.html)

## API Endpoints

### Authentication (Public)

| Method | Endpoint | Description |
|:---|:---|:---|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/login` | Login and receive tokens |
| POST | `/api/v1/auth/refresh` | Refresh an access token |

### Authentication (Protected)

| Method | Endpoint | Description |
|:---|:---|:---|
| POST | `/api/v1/auth/logout` | Invalidate refresh token |

### User Profile

| Method | Endpoint | Description |
|:---|:---|:---|
| GET | `/api/v1/users/me` | Get current user profile |

### Tasks

| Method | Endpoint | Description |
|:---|:---|:---|
| POST | `/api/v1/tasks` | Create a task |
| GET | `/api/v1/tasks` | List tasks (paginated) |
| GET | `/api/v1/tasks/{id}` | Get a task by ID |
| PUT | `/api/v1/tasks/{id}` | Update a task |
| DELETE | `/api/v1/tasks/{id}` | Delete a task |
| GET | `/api/v1/tasks/search` | Search by title/category |
| GET | `/api/v1/tasks/filter` | Filter by status/priority/category |

### Administration (ROLE_ADMIN)

| Method | Endpoint | Description |
|:---|:---|:---|
| GET | `/api/v1/admin/users` | List all users |
| GET | `/api/v1/admin/tasks` | List all tasks |
| DELETE | `/api/v1/admin/tasks/{id}` | Delete any task |

## Database Schema

Four tables managed by Flyway migrations:

- `roles` — Security roles (ROLE_USER, ROLE_ADMIN)
- `users` — User accounts with BCrypt password hashes
- `tasks` — User-owned tasks with status, priority, and category
- `refresh_tokens` — Stored refresh tokens (one per user)

## Testing

```bash
./gradlew test
```

## Project Structure

```
src/main/java/com/mohamedsalah/taskmanager/
├── config/          # Application configuration
├── controller/      # REST controllers
├── dto/             # Request and response DTOs
├── entity/          # JPA entities
├── enums/           # Domain enumerations
├── exception/       # Custom exceptions and global handler
├── mapper/          # MapStruct mappers
├── repository/      # Spring Data JPA repositories
├── security/        # JWT and Spring Security
├── service/         # Business logic interfaces and implementations
├── specification/   # JPA Specification builders
├── util/            # Utility classes
└── validation/      # Custom validators
```
