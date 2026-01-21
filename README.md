
# QMEETX

**QMEETX** is a multi-tenant SaaS platform for managing **appointments, bookings, and queues** for businesses like hospitals, salons, car washes, and more. It is built on a **Java Spring Boot microservices architecture**, designed for **scalability, real-time updates, and production readiness**.

The system integrates **API Gateway**, **auth & OTP services**, **shared modules**, and supports asynchronous microservices communication for high performance.

---

## Current Progress
| Module / Section        | Status         |
|-------------------------|----------------|
| Booking Section         | Completed      |
| Queue Section           | Completed      |
| Auth Service            | Completed      |
| OTP Service             | Completed      |
| User Service            | Completed      |
| API Gateway             | Completed      |
| QmeetShared             | Completed      |
|RealTime                 |Completed       | 
---

## Core Modules / Microservices

### API Gateway
- Entry point for all client requests.
- Handles routing to appropriate services.
- Supports asynchronous requests for improved performance.
- Integrates JWT-based authentication and authorization.

### User Service
- Manages user profiles and multi-tenant data.
- Handles business-specific configurations.
- Works asynchronously with other services via API Gateway and Kafka.

### Auth Service
- JWT-based authentication and role management.
- Integrates with OTP Service for secure login.
- Supports multi-tenant authentication.

### OTP Service
- Handles OTP generation and validation for secure logins.
- Works asynchronously with Auth Service and API Gateway.

### QmeetShared
- Contains shared utilities, constants, DTOs, and common logic used across all services.
- Ensures consistency and reduces code duplication.

### Booking Section (In Progress)
- Will handle appointment scheduling, multi-tenant booking, conflict resolution, and real-time updates.
- Search & discovery using PostgreSQL FTS + pg_trgm + Redis caching.

### Queue Section (In Progress)
- Will manage real-time queues, OTP-based check-in, multi-service queues, and analytics.
- Live status updates for customers and staff.

---

## Architecture Overview

```
        ┌───────────────┐
        │   Client App  │
        └──────┬────────┘
               │
               ▼
        ┌───────────────┐
        │  API Gateway  │  (async support, routing, JWT auth)
        └──────┬────────┘
               │
   ┌───────────┴───────────┐
   │                       │
   ▼                       ▼
┌────────────┐         ┌────────────┐
│ Auth Svc   │         │ User Svc   │
│ (JWT+OTP)  │         │ Profile/   │
└─────┬──────┘         │ Tenant Mgmt│
      │                └─────┬──────┘
      ▼                      │
┌────────────┐                │
│ OTP Svc    │                │
└────────────┘                │
                               ▼
                        ┌────────────┐
                        │ Booking/   │
                        │ Queue Svc  │ (In Progress)
                        └────────────┘
```

- Services communicate **asynchronously** using Kafka/Redis where needed.  
- Shared module (`QmeetShared`) provides DTOs, utilities, and constants for consistency.  

---

## Tech Stack
- **Backend:** Java Spring Boot, Spring WebFlux  
- **Database:** PostgreSQL (FTS + pg_trgm + PostGIS)  
- **Caching & Messaging:** Redis, Kafka  
- **Real-time Communication:** WebSocket  
- **Authentication:** JWT + OTP  
- **API Gateway:** Spring Cloud Gateway (async support)  
- **Containerization & Deployment:** Docker, optional AWS deployment  

---

## Getting Started

### Prerequisites
- Java 17+  
- Maven or Gradle  
- PostgreSQL  
- Redis  
- Docker (optional for local microservices)  

### Installation
1. Clone the repository:
```bash
git clone https://github.com/your-username/qmeetx.git
```
2. Navigate to each microservice and build:
```bash
cd auth-service
mvn clean install
```
3. Configure environment variables (DB URL, JWT secret, Redis URL) in `.env` or `application.properties`.  
4. Run services individually:
```bash
java -jar target/auth-service.jar
```

---

## Contributing
1. Fork the repository.  
2. Create a new feature branch: `git checkout -b feature-name`.  
3. Commit your changes: `git commit -m "Description of feature"`.  
4. Push to branch: `git push origin feature-name`.  
5. Open a pull request.

---



---

## Contact
For queries or suggestions, reach out at `haroonurrasheed1212@gmail.com`.
