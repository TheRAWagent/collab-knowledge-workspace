# User Service

Manages user profiles and identity metadata. Provides gRPC interfaces for internal service-to-service communication.

## Features

- **Profile Management**: Store and retrieve user-specific information.
- **gRPC Integration**: High-performance internal API for other services to look up user data.
- **Caching**: Redis integration for faster profile lookups.
- **Scalability**: Designed as an independent microservice.

## Tech Stack

- **Spring Boot 4.0**
- **Spring Data JPA**
- **Spring gRPC** (Server implementation)
- **PostgreSQL**
- **Redis**
- **Java 25**

## Getting Started

### Prerequisites

- Java 25
- PostgreSQL 14+
- Redis 6+

### Local Run

```bash
./gradlew bootRun
```

## Configuration

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/ckw_user` |
| `SPRING_REDIS_HOST` | Redis server host | `localhost` |
| `GRPC_SERVER_PORT` | Port for gRPC traffic | `9090` |

## API & gRPC

### Internal gRPC Methods
- `GetUserById`: Retrieve user profile by ID.
- `FindUserByEmail`: Locate user by email address.

### REST Endpoints
- `GET /user/{id}`: Public profile retrieval (routed via Gateway).
