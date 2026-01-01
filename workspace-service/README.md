# Workspace Service

Manages organizational units (Workspaces) and document hierarchies within the Collaborative Knowledge Workspace.

## Features

- **Workspace Management**: Creation, deletion, and modification of collaborative spaces.
- **Member Management**: Handling user access and roles within workspaces.
- **gRPC Server**: Provides `PageAccessService` for API Gateway validation.
- **gRPC Client**: Communicates with `User Service` for user info.

## Tech Stack

- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring gRPC**
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
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/ckw_workspace` |
| `GRPC_CLIENT_USER_SERVICE_ADDRESS` | Address of User Service gRPC | `static://localhost:9090` |
| `GRPC_SERVER_PORT` | gRPC Server Port | `9091` |

## API Highlights

- `GET /workspaces`: List user's workspaces.
- `POST /workspaces`: Create a new workspace.
- `GET /workspaces/{id}`: Get workspace details and members.
