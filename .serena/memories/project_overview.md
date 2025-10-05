# Greetings Project Overview

## Purpose
This is a demonstration project showcasing microservices architecture and various modern software development patterns. The system manages greeting messages through multiple interconnected services.

## Architecture
The project follows a microservices architecture with three main components:

### Services
1. **greetings-service**: Main service for creating and managing greetings
   - Uses hexagonal architecture with modules: domain, application, persistence, rest, producer, bootstrap
   - Produces events to Kafka
   - Provides REST API for greeting operations
   - Uses PostgreSQL database

2. **greetings-stat-service**: Service for tracking greeting statistics
   - Reactive implementation with Spring WebFlux
   - Consumes events from Kafka
   - Provides statistics REST API
   - Uses PostgreSQL database
   - Has client module for calling greetings-service

3. **greetings-ui**: Vue 3-based frontend
   - Vue 3 with TypeScript and Vite
   - Uses PrimeVue component library and Tailwind CSS
   - Interacts with both services via REST APIs

### Infrastructure
- **Kafka**: Message broker for inter-service communication (greeting_topic)
- **PostgreSQL**: Databases for both services
- **Docker**: Containerization support

## Patterns & Concepts Demonstrated
- Hexagonal Architecture
- Multimodule Spring Boot projects
- REST API (Spring MVC + Spring WebFlux)
- Kafka Consumer/Producer pattern
- Reactive programming
- Contract-Driven Development (CDC) with Pact
- BDD testing with Cucumber
- Comprehensive testing strategies