# Voting System Refactored to Microservices

This project is a refactored version of a monolith voting system into a High Availability microservices architecture.

## Architecture

- **API Gateway**: Entry point for all requests. Handles routing and JWT validation.
- **Auth Service**: Manages passwordless login, user roles, and JWT issuance. Uses `auth_schema`.
- **Voting Service**: Stateless ingestion service. Accepts votes and publishes them to RabbitMQ.
- **Result Service**: Consumes vote events from RabbitMQ, persists them to `result_schema`, and maintains live results in Caffeine cache. Handles voting session management.
- **RabbitMQ**: Message broker for decoupled asynchronous communication.
- **PostgreSQL**: Single database container with separate schemas for different services.

## Prerequisites

- Docker and Docker Compose
- Java 21 (optional, for local build)

## Getting Started

1. Clone the repository.
2. Ensure you have the necessary environment variables (e.g., for email if needed).
3. Build and run the system using Docker Compose:

```bash
docker-compose up --build
```

The system will be available at `http://localhost:8080`.

## API Endpoints

### Auth Service (via Gateway)
- `POST /auth/login`: Request a verification code.
- `POST /auth/verify`: Verify code and receive JWT.

### Voting Service (via Gateway)
- `POST /votes`: Submit a vote (requires JWT with `USER` role).

### Result Service (via Gateway)
- `GET /results/live`: Get live voting results.
- `GET /results/final/{sessionId}`: Get final results for a session.
- `GET /session/active`: Get the currently active voting session.

### Admin (via Gateway, requires JWT with `ADMIN` role)
- `GET /admin/session`: List all sessions.
- `POST /admin/session`: Create a new session.
- `POST /admin/session/activate/{id}`: Activate a session.
- `POST /admin/session/close/{id}`: Close a session.
- `DELETE /admin/session/{id}`: Delete a session.

## HA and Scaling

- **Stateless Ingestion**: The Voting Service doesn't depend on the database, allowing it to remain operational even if the database is down.
- **Asynchronous Persistence**: Votes are queued in RabbitMQ and processed by the Result Service, ensuring high availability and fault tolerance.
- **Database Separation**: Services use separate schemas, following microservices best practices.
- **Caching**: Caffeine cache in the Result Service ensures fast access to live results.
