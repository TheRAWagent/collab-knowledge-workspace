# Page Service

Reactive persistence layer for documents and pages. Handles high-concurrency document modifications and storage.

## Features

- **Reactive Architecture**: Built with Spring WebFlux and R2DBC for non-blocking I/O.
- **Document Persistence**: Stores document state and Prosemirror JSON content.
- **Database Migrations**: Automatic schema management with Flyway.
- **Snapshot Support**: Support for document snapshots used by the collaboration service.

## Tech Stack

- **Spring Boot 3.5.9** (WebFlux)
- **Spring Data R2DBC**
- **PostgreSQL** (with R2DBC driver)
- **Flyway**
- **Java 25**

## Getting Started

### Prerequisites

- Java 25
- PostgreSQL 14+

### Local Run

```bash
./gradlew bootRun
```

## Configuration

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_R2DBC_URL` | R2DBC connection URL | `r2dbc:postgresql://localhost:5432/ckw_page` |
| `SPRING_FLYWAY_URL` | JDBC URL for Flyway | `jdbc:postgresql://localhost:5432/ckw_page` |
| `SPRING_R2DBC_USERNAME` | R2DBC username | `admin_user` |
| `SPRING_R2DBC_PASSWORD` | R2DBC password | `password` |
| `SPRING_SQL_INIT_MODE` | SQL initialization mode | `never` |

## API Highlights

- `GET /workspace/{wid}/documents/{id}`: Fetch document content.
- `POST /workspace/{wid}/documents`: Create new document.
- `GET /workspace/{wid}/documents/{id}/snapshot`: Retrieval of document snapshots for Hocuspocus.
