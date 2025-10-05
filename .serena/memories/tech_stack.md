# Technology Stack

## Backend Services
- **Java**: Version 24 (Temurin distribution recommended)
- **Spring Boot**: 3.5.4
- **Spring Framework**: MVC (greetings-service) and WebFlux (stat-service)
- **Build Tool**: Maven 3.8+
- **Databases**: PostgreSQL
- **Message Broker**: Apache Kafka

## Frontend
- **Framework**: Vue 3.5+
- **Language**: TypeScript 5.5+
- **Package Manager**: yarn
- **Build Tool**: Vite 6.3+
- **UI Library**: PrimeVue 4.3+ with Tailwind CSS 4.1+
- **Router**: Vue Router 4

## Testing Framework
- **Unit Testing**: JUnit 5 (5.13.2), AssertJ, Mockito
- **Integration Testing**: Spring Boot Test, Testcontainers (1.21.2)
- **BDD Testing**: Cucumber (7.23.0)
- **Contract Testing**: Pact (4.6.17)
- **Mocking**: Wiremock (3.13.1)
- **Frontend Testing**: Vitest (Vue), Vue Test Utils, Pact for contracts, Cypress for E2E
- **Test Utilities**: Awaitility (4.3.0), ToStringVerifier (1.4.8)

## DevOps & Quality
- **Containerization**: Docker with Maven plugin (0.46.0)
- **Code Coverage**: JaCoCo (0.8.13)
- **Code Quality**: SonarCloud integration
- **CI/CD**: GitHub Actions
- **Linting**: ESLint (frontend), Maven checkstyle patterns

## Development Tools
- **Parallel Testing**: Maven Surefire/Failsafe plugins with parallel execution
- **Embedded Testing**: EmbeddedKafka for messaging tests
- **System**: macOS (Darwin) compatible