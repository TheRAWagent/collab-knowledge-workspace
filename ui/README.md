# CKW Frontend (UI)

A modern, responsive React-based interface for the Collaborative Knowledge Workspace.

## Features

- **Collaborative Editor**: Full-featured rich text editor with real-time sync (TipTap + Yjs).
- **Workspace Explorer**: Manage workspaces, folders, and documents.
- **Dynamic Routing**: Type-safe routing using TanStack Router.
- **Data Querying**: Efficient data fetching and caching with TanStack Query.
- **Theming**: Sleek UI built with Tailwind CSS and Radix UI primitives.
- **Authentication**: Secure login/registration flows integrated with Auth Service.

## Tech Stack

- **React 18+**
- **Vite**
- **TypeScript**
- **TanStack Router**
- **TanStack Query**
- **TipTap** (Editor)
- **Tailwind CSS**
- **Lucide React** (Icons)

## Getting Started

### Prerequisites

- Node.js 18+
- pnpm or npm

### Installation

```bash
npm install
```

### Development

```bash
npm run dev
```

The app will be available at `http://localhost:5173`.

### Build

```bash
npm run build
```

## Project Structure

- `src/components`: Reusable UI components (Shared).
- `src/modules`: Feature-specific logic and components (Auth, Workspaces, Pages).
- `src/hooks`: Custom React hooks for data fetching and state.
- `src/routes`: Route definitions for TanStack Router.

## Environment Variables

Create a `.env` file in the root:

```env
VITE_API_GATEWAY_URL=http://localhost:8080
VITE_COLLAB_SERVICE_URL=ws://localhost:8081
```
