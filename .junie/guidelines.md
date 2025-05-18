# Greetings Project Development Guidelines

This document provides essential information for developers working on the Greetings project.

## Build/Configuration Instructions

### Prerequisites

- Java 24 (Temurin distribution recommended)
- Maven 3.8+
- Node.js 18+ (for UI component)
- Docker (for containerization and integration tests)

### Building the Project

#### Backend Services
The project uses Maven for building the backend services. The parent POM is located at `greetings-parent/pom.xml`.

```bash
# Build all services
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Build with coverage reporting
mvn clean install -Pcoverage
```

#### Frontend (UI)
The UI component uses npm for building:

```bash
cd greetings-ui
npm install
npm run build
```

### Docker Image Creation
Docker images can be built using the Maven Docker plugin:

```bash
# Build Docker image for Greetings Service
cd greetings-service
mvn clean package -PbuildImage -DskipTests

# Build Docker image for Stats Service
cd greetings-stat-service
mvn clean package -PbuildImage -DskipTests
```

## Testing Information

### Test Structure
The project employs multiple testing approaches:

1. **Unit Tests**: Located in `src/test/java` of each module
   - Uses JUnit 5, AssertJ, and Mockito
   - Follow the Arrange-Act-Assert pattern

2. **Integration Tests**: 
   - Spring Slice Tests with annotations like `@SpringBootTest`
   - Kafka integration tests with `@EmbeddedKafka`
   - Service mocking with Wiremock

3. **BDD Tests**: 
   - Cucumber features in `bdd/features`
   - Step definitions in `**/glue` packages
   - Multiple implementations at different levels (domain, application, bootstrap, rest, e2e)

4. **Contract Tests**:
   - Consumer-driven contract tests with Pact
   - Both REST API and Kafka message contracts

### Running Tests

#### Unit Tests
```bash
# Run unit tests for a specific module
cd <module-directory>
mvn test

# Run a specific test class
mvn test -Dtest=SimpleGreetingTest
```

#### Integration Tests
```bash
# Run integration tests for a specific module
cd <module-directory>
mvn verify -DskipUTs
```

#### BDD Tests
```bash
# Run BDD tests
cd <module-directory>
mvn verify -Dcucumber.filter.tags="@e2e"
```

#### Contract Tests
```bash
# Run Pact consumer tests
cd greetings-stat-service/stat-client
mvn test -Dtest=*Pact*

# Run Pact provider tests
cd greetings-service/greetings-bootstrap
mvn test -Dtest=*Pact*
```

### Example: Creating a Simple Test

Here's an example of a simple unit test:

```java
package net.bakaar.greetings.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleGreetingTest {

    @Test
    void should_create_greeting_command_with_valid_values() {
        // Arrange
        String type = "birthday";
        String name = "John";

        // Act
        CreateGreetingCommand command = new CreateGreetingCommand(type, name);

        // Assert
        assertThat(command).isNotNull();
        assertThat(command.type()).isEqualTo(type);
        assertThat(command.name()).isEqualTo(name);
    }
}
```

## Additional Development Information

### Architecture
The project follows a hexagonal architecture pattern:
- Domain logic is isolated in domain modules
- Adapters connect the domain to external systems (REST, persistence, messaging)
- Application services orchestrate use cases

### Code Style
- Follow standard Java code style conventions
- Use records for DTOs and value objects
- Follow the Arrange-Act-Assert pattern in tests
- Use descriptive test method names that explain the behavior being tested

### Module Structure
- **greetings-service**: Main service for creating and managing greetings
  - greetings-domain: Core domain logic
  - greetings-application: Application services
  - greetings-persistence: Database adapters
  - greetings-rest: REST API
  - greetings-producer: Kafka message production
  - greetings-bootstrap: Application bootstrap and configuration

- **greetings-stat-service**: Service for tracking greeting statistics
  - stat-domain: Core domain logic
  - stat-application: Application services
  - stat-persistence: Database adapters
  - stat-rest: REST API
  - stat-consumer: Kafka message consumption
  - stat-client: Client for greetings-service
  - stat-bootstrap: Application bootstrap and configuration

- **greetings-ui**: Angular-based frontend

### Debugging
- Spring Boot Actuator endpoints are available for monitoring
- Logging configuration is in the logback-spring.xml files
- Integration tests use embedded databases and Kafka for isolation
