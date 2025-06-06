# Greetings Project Development Guidelines

This document provides essential information for developers working on the Greetings project.

## Build/Configuration Instructions

### Prerequisites

- Java 25 (Temurin distribution recommended)
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

The UI component is built with Vue 3, TypeScript, and Vite. It uses yarn for package management:

```bash
cd greetings-ui
yarn install
yarn build
```

For development, you can use:

```bash
cd greetings-ui
yarn dev
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

5. **End-to-End Tests**:
    - Cypress tests in `greeting-ui-e2e` directory
    - Uses TypeScript for better type safety and developer experience
    - Implements Cucumber/Gherkin for BDD-style tests
    - Tests the UI application against the feature files in `bdd/features`
   - Project structure:
     ```
     greeting-ui-e2e/
     ├── cypress/
     │   ├── e2e/
     │   │   └── step_definitions/
     │   │       ├── GreetingsCreation.steps.ts
     │   │       ├── GreetingsUpdate.steps.ts
     │   │       └── GreetingsStats.steps.ts
     │   └── support/
     │       ├── commands.ts
     │       └── e2e.ts
     ├── cypress.config.ts
     ├── package.json
     ├── tsconfig.json
     └── README.md
     ```

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

#### End-to-End Tests

```bash
# Install dependencies
cd greeting-ui-e2e
yarn install

# Run Cypress tests in headless mode (starts the UI application automatically)
yarn test

# Run Cypress tests with the Cypress UI (starts the UI application automatically)
yarn test:open

# If you want to run the UI application separately
cd greetings-ui
yarn dev

# Then in another terminal
cd greeting-ui-e2e
yarn cypress:run  # For headless mode
# or
yarn cypress:open  # For Cypress UI
```

### Writing End-to-End Tests

#### Feature Files

The feature files are located in the `bdd/features` directory and use Gherkin syntax. For example:

```gherkin
Feature: Creation of a Greeting Message

  Rule: There are three type of Greeting, Birthday, Anniversary and Christmas

    Scenario: Simple Greeting
      When I create an anniversary greeting for Charles
      Then I get the message "Joyful Anniversary Charles !"
      Then a Greeting is created
```

#### Step Definitions

Step definitions are located in the `cypress/e2e/step_definitions` directory and are written in TypeScript. They map the
Gherkin steps to Cypress commands. For example:

```typescript
import {When, Then} from '@badeball/cypress-cucumber-preprocessor';

When('I create an anniversary greeting for Charles', () => {
    cy.createGreeting('anniversary', 'Charles');
});

Then('I get the message {string}', (message: string) => {
    cy.verifyGreetingMessage(message);
});

Then('a Greeting is created', () => {
    cy.verifyGreetingCreated();
});
```

#### Custom Commands

Custom Cypress commands are defined in the `cypress/support/commands.ts` file. These commands encapsulate common actions
and assertions used in the step definitions. For example:

```typescript
Cypress.Commands.add('createGreeting', (type: string, name: string) => {
    cy.visit('/');
    cy.get('[data-cy=greeting-type]').select(type);
    cy.get('[data-cy=greeting-name]').type(name);
    cy.get('[data-cy=create-greeting]').click();
});
```

### End-to-End Testing Best Practices

1. **Use TypeScript**: TypeScript provides better type safety and developer experience.
2. **Use Custom Commands**: Encapsulate common actions and assertions in custom commands.
3. **Use Data Attributes**: Use `data-cy` attributes to select elements in the UI.
4. **Keep Step Definitions Simple**: Step definitions should be simple and focused on a single action or assertion.
5. **Use Before and After Hooks**: Use hooks to set up and clean up test state.
6. **Use Aliases**: Use Cypress aliases to store and retrieve values between steps.
7. **Use Fixtures**: Use fixtures to provide test data.
8. **Use Screenshots and Videos**: Enable screenshots and videos to help debug test failures.
9. **Use Retry Ability**: Use Cypress's retry ability to handle asynchronous operations.
10. **Follow Cypress Best Practices**: Refer to
    the [Cypress Best Practices](https://docs.cypress.io/guides/references/best-practices) documentation.

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

- **greetings-ui**: Vue 3-based frontend with TypeScript and Vite

### Debugging
- Spring Boot Actuator endpoints are available for monitoring
- Logging configuration is in the logback-spring.xml files
- Integration tests use embedded databases and Kafka for isolation
