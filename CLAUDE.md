# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Prerequisites

- Java 25 (Temurin distribution recommended)
- Maven 3.8+
- Node.js 18+ and Yarn (for UI)
- Docker (required for integration tests via Testcontainers)

## Build Commands

### Backend Services

The parent POM lives at `greetings-parent/pom.xml`. Each service has its own POM at `greetings-service/pom.xml` and
`greetings-stat-service/pom.xml`.

```bash
# Build a service (from its directory)
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Build with coverage
mvn clean install -Pcoverage

# Build Docker image
mvn clean package -PbuildImage -DskipTests
```

### Frontend (Angular)

```bash
cd greetings-ui
yarn install
yarn run build   # includes lint
yarn start       # dev server at http://localhost:4200
```

## Testing

### Run tests for a single module

```bash
# Unit tests only (from module directory)
mvn test

# Run a specific test class
mvn test -Dtest=SimpleGreetingTest

# Integration tests only (skips unit tests)
mvn verify -DskipUTs

# BDD tests with a specific tag
mvn verify -Dcucumber.filter.tags="@e2e"
```

### Contract tests (Pact)

```bash
# Consumer side (stat-client generates pact files)
cd greetings-stat-service/stat-client
mvn test -Dtest=*Pact*

# Provider side (greetings-bootstrap verifies pact files)
cd greetings-service/greetings-bootstrap
mvn test -Dtest=*Pact*
```

### UI tests

```bash
cd greetings-ui
yarn test          # Jest unit tests with coverage
yarn run pact      # Pact consumer contract tests (runs serially)
```

### Full test suite

```bash
./run-all-tests.sh [--skip-ui] [--skip-e2e] [--no-parallel]
```

## Architecture

The project is a microservices demo with three services communicating via REST and Kafka:

```
greetings-ui → greetings-service → Kafka → greetings-stat-service
greetings-ui → greetings-stat-service → greetings-service (REST)
```

Both backend services follow **Hexagonal Architecture**. Each service's module layout maps to hexagonal layers:

| Module                      | Role                                                                    |
|-----------------------------|-------------------------------------------------------------------------|
| `*-domain`                  | Core domain entities, value objects, port interfaces                    |
| `*-application`             | Use-case orchestration (application services)                           |
| `*-rest`                    | REST adapter (inbound) — Spring WebMVC for greetings, WebFlux for stats |
| `*-persistence`             | JPA persistence adapter (outbound)                                      |
| `*-producer` / `*-consumer` | Kafka adapters (outbound/inbound)                                       |
| `*-bootstrap`               | Spring Boot application assembly — wires all modules together           |
| `stat-client`               | REST client adapter used by greetings-ui and stat-service               |

The `greetings-stat-service` is fully reactive (Spring WebFlux + Project Reactor). The `greetings-service` uses Spring
WebMVC.

## Test Strategy

The project uses **multiple testing levels**, each with Cucumber BDD scenarios from `bdd/features/`:

- **Domain-level BDD** (`greetings-domain`, `stat-domain`): pure domain logic, no Spring context
- **Application-level BDD** (`greetings-application`): use cases with mocked ports
- **Slice tests** (`greetings-rest`, `greetings-persistence`): Spring slices (`@WebMvcTest`, `@DataJpaTest`)
- **Bootstrap-level BDD** (`greetings-bootstrap`, `stat-bootstrap`): full Spring context with Testcontainers (
  PostgreSQL) and `@EmbeddedKafka`
- **E2E tests** (`greetings-e2e`): Docker Compose-based full-stack tests

BDD step definitions live in `**/glue` packages within the test source of each module. Integration test classes use the
`IT` suffix (picked up by maven-failsafe-plugin).

## Code Conventions

- Use Java records for DTOs, commands, and value objects
- Test methods named `should_<behavior>_when_<condition>` using Arrange-Act-Assert
- Domain port interfaces (e.g., `GreetingRepository`, `EventEmitter`) are defined in the domain module; adapters
  implement them in outer modules
- Sonar coverage excludes `*Properties.java`, `*Configuration.java`, and `*Application.java`
