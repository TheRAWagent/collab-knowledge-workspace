# Collaboration Service

Real-time synchronization engine for the Collaborative Knowledge Workspace, powered by Hocuspocus and Yjs.

## Features

- **Real-time Editing**: WebSocket-based synchronization using the Hocuspocus server.
- **CRDT-based Conflict Resolution**: Powered by Yjs for seamless multi-user collaboration.
- **Prosemirror Integration**: Direct support for Prosemirror document schemas and JSON formats.
- **Persistence Hooks**: Automatic saving of document state to `Page Service` via **HTTP**.
- **Session Management**: Redis extension for tracking active editors and document states.

## Tech Stack

- **Bun** (Runtime)
- **TypeScript**
- **Hocuspocus Server**
- **Yjs**
- **Redis**
- **Axios** (for internal API calls)

## Getting Started

### Prerequisites

- [Bun](https://bun.sh/) installed locally.
- Redis server running.
- `Page Service` reachable for persistence.

### Installation

```bash
bun install
```

### Development

```bash
bun run dev
```

### Production Build

```bash
bun build src/index.ts --target bun --outdir ./dist
```

## Configuration

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | WebSocket server port | `8080` |
| `REDIS_URL` | Redis connection string | `redis://localhost:6379` |
| `PAGE_SERVICE_URL` | Base URL for the Page Service | `http://localhost:8081` |

## Architecture
This service acts as the central hub for WebSockets, accessed via the **API Gateway**.

Flow:
1. Client connects via Gateway (`ws://gateway:8080/collaboration`).
2. Gateway handles **JWT Validation**.
3. Connection is forwarded to this service.
4. `onLoadDocument` hook fetches the latest snapshot from `Page Service`.
5. Changes are synced in real-time across all connected clients.
6. `onStoreDocument` triggers an **HTTP POST** request to `Page Service` to persist changes.
