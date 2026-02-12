# Software Requirements Specification (SRS) - Real-time Microservices Voting System

## 1. Introduction
This document outlines the requirements for a high-availability, real-time voting system designed as a microservices architecture. The system allows users to vote in active sessions and view results in real-time.

## 2. Architecture Overview
The system follows an event-driven, decoupled architecture (High Availability Variant C).
- **Backend**: Spring Boot Microservices (Java 21).
- **Messaging**: RabbitMQ for asynchronous vote processing.
- **Database**: PostgreSQL with separate schemas for services.
- **Gateway**: Spring Cloud Gateway for routing and security.
- **Frontend**: Vue 3 (Composition API) Single Page Application.

## 3. Microservices Components

### 3.1 Auth Service
- Handles passwordless authentication via email.
- Issues JWT tokens with user roles (`ROLE_USER`, `ROLE_ADMIN`).
- Manages user verification.

### 3.2 Voting Service (Stateless)
- High-performance ingestion layer.
- Validates requests and pushes `VoteEvent` to RabbitMQ.
- Does NOT connect to the database to ensure maximum throughput and low latency.

### 3.3 Result Service
- Consumes events from RabbitMQ.
- Persists votes into PostgreSQL (`result_schema`).
- Manages voting sessions (Creation, Activation, Closing).
- Provides live results via Caffeine cache and final results.

### 3.4 API Gateway
- Central entry point (Port 8080).
- Handles JWT validation and Role-Based Access Control (RBAC).
- Manages CORS for the frontend.

## 4. Frontend Client (Vue 3)
The frontend is built using **Vue 3** with the following requirements:
- **State Management**: Pinia for managing auth state and live results.
- **Router**: Vue Router for navigation (Login, Voting, Admin Dashboard).
- **Styling**: Tailwind CSS or Vuetify for a modern, responsive UI.
- **Real-time Updates**: Polling or WebSockets to display live voting progress from `/results/live`.
- **Authentication**: Stores JWT in `localStorage` or `HttpOnly` cookies and includes it in the `Authorization` header.

## 5. Functional Requirements
1. **User Authentication**: Users must log in using their `@alatoo.edu.kg` email.
2. **Voting**: Authenticated users can vote exactly once per active session.
3. **Real-time Results**: Live results must be available to all users.
4. **Admin Management**: Admins can create sessions, add options, and control session status (DRAFT, ACTIVE, CLOSED).

## 6. Non-Functional Requirements
- **High Availability**: Voting continues even if the database or result service is temporarily down.
- **Scalability**: Stateless voting services can be scaled horizontally.
- **Security**: JWT-based security with strict role checking at the Gateway level.
- **Data Integrity**: Duplicate votes are prevented at the consumer level (database constraints + service logic).
