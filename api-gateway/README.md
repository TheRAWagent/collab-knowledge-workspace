# API Gateway

The entry point for all client requests in the Collaborative Knowledge Workspace (CKW) platform. It handles request routing, authentication validation, and protocol conversion.

## Features

- **Request Routing**: Routes traffic to appropriate microservices based on path patterns.
- **Security**: Validates JWT tokens using a custom `JwtValidation` filter.
- **Access Control**: Validates page access permissions via **gRPC** calls to `Workspace Service`.
- **Protocol Conversion**: Support for both HTTP and gRPC communication.
- **CORS Handling**: Centralized CORS configuration for the frontend.
- **API Documentation Aggregation**: Routes to OpenAPI docs of individual services.

## Tech Stack

- **Spring Boot 3.5.7**
- **Spring Cloud Gateway** (WebFlux-based)
- **Spring gRPC** (Client)
- **Java 25**

## Getting Started

### Prerequisites

- Java 25
- Access to other microservices (Auth, User, Page, Workspace)

### Local Run

```bash
./gradlew bootRun
```

The gateway runs on port `8080` by default.

## Configuration

Key environment variables/properties:

- `SERVER_PORT`: Port for the gateway (default: 8080)
- `SPRING_CLOUD_GATEWAY_ROUTES`: Route configurations (defined in `application.yaml`)

### Routes Overview

| Service | Route Pattern | Target URL |
|---------|---------------|------------|
| Auth | `/auth/**` | `http://ckw-auth-service:8080` |
| User | `/user/**` | `http://ckw-user-service:8080` |
| Workspace | `/workspaces/**` | `http://ckw-workspace-service:8080` |
| Page | `/workspace/{wid}/documents/**` | `http://ckw-page-service:8080` |
| Collaboration | `/collaboration/**` | `ws://ckw-collaboration-service:8080` |

## API Documentation

Aggregated documentation is available at:
- `/api-docs/auth`
- `/api-docs/user`
- `/api-docs/workspace`
- `/api-docs/page`
