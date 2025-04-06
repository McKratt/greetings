# Demonstration Project

## Builds

[![Build Status](https://github.com/McKratt/greetings/actions/workflows/build.yml/badge.svg)](https://github.com/McKratt/greetings/actions)

### Greetings UI

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=greetings-ui&metric=alert_status)](https://sonarcloud.io/dashboard?id=greetings-ui)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=greetings-ui&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=greetings-ui)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=greetings-ui&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=greetings-ui)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=greetings-ui&metric=coverage)](https://sonarcloud.io/dashboard?id=greetings-ui)

### Greetings Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=greetings-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=greetings-service)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=greetings-service&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=greetings-service)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=greetings-service&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=greetings-service)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=greetings-service&metric=coverage)](https://sonarcloud.io/dashboard?id=greetings-service)

### Stat Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=greetings-stat-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=greetings-stat-service)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=greetings-stat-service&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=greetings-stat-service)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=greetings-stat-service&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=greetings-stat-service)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=greetings-stat-service&metric=coverage)](https://sonarcloud.io/dashboard?id=greetings-stat-service)

## Architecture overview

```mermaid
graph TD
%% Define nodes
    greetings_ui["greetings-ui: Greetings User Interface"]
    greetings["greetings-service: Create Greetings Messages"]
    greetings_db["Greetings DB: PostgreSQL"]
    stats["greetings-stats-service: Records Statistics"]
    stats_db["Statistics DB: PostgreSQL"]
    kafka["Kafka: greeting_topic"]
%% Relationships
    greetings_ui -->|" GET/POST/PUT "| greetings
    greetings_ui -->|" GET /stats "| stats
    greetings -->|" Reads/Writes "| greetings_db
    stats -->|" Reads/Writes "| stats_db
    greetings -->|" Produces Events "| kafka
    stats -->|" Consumes Events "| kafka
    stats -->|" GET /greetings/{id} "| greetings
```

## List of patterns / concepts /technics demonstrated

* Hexagonal Architecture
* Multimodules Spring Boot Project
* REST API with Spring MVC ([greeting service](./greetings-service)) and Spring Web
  Reactive ([stat service](./greetings-stat-service))
* Kafka Consumer ([stat service](./greetings-stat-service)) / producer ([greeting service](./greetings-service))
* Reactive implementation ([stat service](./greetings-stat-service))
* Test : Unit tests with Mockito and AssertJ
* Test : Integration tests with Spring Slice Tests Annotations
* Test : Integration tests with `@EmbeddedKafka`
* Test : Integration tests with [Wiremock](https://wiremock.org/) and Spring Cloud Contract Stub Runner
* Test : CDC tests with [Pact](https://docs.pact5.io)
* Test : BDD tests with Cucumber and JUnit 5

## TODOs

* [ ] Implement Error Handling in API to customize responses
* [ ] Implement [Outbox pattern](https://microservices.io/patterns/data/transactional-outbox.html) for greeting-producer
* [ ] Create a UI to interact with APIs
    * [ ] Create an SSE endpoint to send events to UI