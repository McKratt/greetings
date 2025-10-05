# Technology Stack

## Backend Services
- **Java**: Version 24 (Temurin distribution recommended)
- **Spring Boot**: 3.5.4
- **Spring Framework**: MVC (greetings-service) and WebFlux (stat-service)
- **Build Tool**: Maven 3.8+
- **Databases**: PostgreSQL
- **Message Broker**: Apache Kafka

## Frontend
- **Framework**: Angular 16
- **Language**: TypeScript 4.9.3
- **Package Manager**: yarn
- **Build Tool**: Angular CLI

## Testing Framework
- **Unit Testing**: JUnit 5 (5.13.2), AssertJ, Mockito
- **Integration Testing**: Spring Boot Test, Testcontainers (1.21.2)
- **BDD Testing**: Cucumber (7.23.0)
- **Contract Testing**: Pact (4.6.17)
- **Mocking**: Wiremock (3.13.1)
- **Frontend Testing**: Jest (Angular), Pact for contracts
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