# Credit Conveyor

This project implements a microservices-based **Credit Conveyor** system for automating credit application processes,
from initial pre-scoring to credit issuance. Designed with scalability, reliability, and maintainability in mind, it
leverages modern technologies and best practices.

## Objectives

- Automate credit application lifecycle, including pre-scoring, scoring, and credit condition calculations.
- Provide a clear and transparent API for client interaction.
- Implement seamless email notifications and document generation.
- Ensure scalability with event-driven architecture and API Gateway pattern.

## Technologies Used

### Core Stack

- **Spring Boot**:
    - Spring Web (RESTful APIs)
    - Spring Data JPA (PostgreSQL integration)
    - Spring Cloud (microservice orchestration)

### Data Management

- **PostgreSQL**: Relational database.
- **Liquibase**: Database versioning and migration.

### Communication

- **Feign Client**: Synchronous inter-service calls.
- **Kafka**: Asynchronous event-driven messaging.

### Documentation

- **OpenAPI/Swagger**: API documentation and generation.

### Utilities

- **MapStruct**: DTO-to-entity mapping.
- **JUnit 4/5** and **Mockito**: Testing

### Deployment & Scalability

- **Docker**: Containerized services.
- **API Gateway**: Encapsulation of internal microservices with simplified client routing.

## Key Features

1. **Pre-Scoring and Scoring:** Evaluate client eligibility and calculate credit conditions, including interest rates
   and payment schedules.
2. **Microservice Architecture:** Modular design with clear separation of responsibilities:
    - **Application Service:** Handles initial client applications and pre-scoring.
    - **Deal Service:** Manages application state, credit issuance, and client interactions.
    - **Conveyor Service:** Calculates credit conditions.
    - **Dossier Service:** Generates and emails documents.
3. **Event-Driven Communication:** Asynchronous handling of credit lifecycle events via Kafka topics.
4. **Email Notifications:** Automated email updates throughout the application process, using SMTP integration.
5. **API Gateway:** Simplifies external API by routing requests to internal services.

## Project Structure

```
credit-conveyor/
├── application-service/    # Pre-scoring and client interaction
├── deal-service/           # Application state management
├── conveyor-service/       # Credit calculations
├── dossier-service/        # Document generation and email notifications
├── gateway/                # API Gateway
```

Each microservice includes controllers, services, repositories, and DTOs. Configuration files are centralized
in `application.yaml`.

## Implementation Overview

1. **Pre-Scoring:** Basic checks on client data to determine eligibility.
2. **Scoring:** Advanced analysis of client creditworthiness.
3. **Credit Calculation:** Determine effective interest rates, payment schedules, and costs.
4. **Document Generation:** Create credit agreements, schedules, and other documents.
5. **Event Handling:** Automate credit lifecycle with Kafka-driven events (e.g., application status updates,
   notifications).