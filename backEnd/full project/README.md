## Tourism Hotel Booking System (SWER313 Project)

This repository contains a **Java + Spring Boot** implementation of the SWER313 course project: a tourism hotel booking backend that starts as a **modular monolith** and later evolves into **microservices** with realistic deployment support.

The project is split into three main steps described in the course PDF:

- **Step 1 – Monolith backend**: hotel catalog, availability & pricing, booking, payment (mocked), notifications, validation, and OpenAPI docs.
- **Step 2 – Microservices**: split into Catalog, Availability/Pricing, Booking, Payment, Notification services, with API gateway, RabbitMQ events, resilience, and docker-compose.
- **Step 3 – Deployment**: CI/CD pipeline, container images, and deployment notes for AWS ECS/Fargate + observability.

Implementation details, design decisions, and how to run the system (monolith and microservices) are documented in separate step-specific markdown files that will be added as the implementation progresses.

