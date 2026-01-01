# Auth Service

Handles user authentication, session management, and JWT token issuance for the Collaborative Knowledge Workspace.

## Features

- **User Authentication**: Support for registration and login.
- **JWT Management**: Issuance and validation of secure JWT tokens.
- **Session Caching**: Uses Redis to store and manage active sessions.
- **Role-Based Access Control**: Foundations for user permissions.
- **Token Caching**: Caches token validation results in Redis (10m TTL) to reduce database load.
- **API Documentation**: Integrated Swagger UI and OpenAPI 3.

## Tech Stack

- **Spring Boot 3.5.7**
- **Spring Security**
- **Spring Data JPA**
- **Spring Data Redis**
- **PostgreSQL**
- **JJWT** (Java JWT)
- **Java 25**

## Getting Started

### Prerequisites

- Java 25
- PostgreSQL 14+
- Redis 6+ (Dev uses `redis:8.2.3-alpine`)

### Local Run

```bash
./gradlew bootRun
```

Runs on port `8080` (or as configured via API Gateway).

## Configuration

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/ckw_auth` |
| `SPRING_DATASOURCE_USERNAME` | Database Username | `admin_user` |
| `SPRING_DATASOURCE_PASSWORD` | Database Password | `password` |
| `SPRING_DATA_REDIS_HOST` | Redis server host | `localhost` (Docker: `ckw-cache`) |
| `JWT_SECRET` | Secret key for JWT signing | - |
| `LOGGING_LEVEL_ROOT` | Root logging level | `info` |

## API Endpoints

- `POST /auth/register`: Create a new user account.
- `POST /auth/login`: Authenticate and receive a JWT.
- `POST /auth/verify`: Verify token validity.

Full API documentation available via Swagger UI when running locally.
