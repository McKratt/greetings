# Code Style and Conventions

## Java Backend Code Style

### Naming Conventions
- **Classes**: PascalCase (e.g., `CreateGreetingCommand`, `SimpleGreeting`)
- **Methods**: camelCase with descriptive names
- **Test Methods**: snake_case with "should_" prefix describing behavior (e.g., `should_create_greeting_command_with_valid_values`)
- **Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE
- **Packages**: lowercase with dots (e.g., `net.bakaar.greetings.domain`)

### Architecture Patterns
- **Hexagonal Architecture**: Clear separation between domain, application, persistence, rest, producer layers
- **Domain-Driven Design**: Rich domain models with clear boundaries
- **Records**: Use Java records for DTOs and value objects (e.g., `CreateGreetingCommand`)
- **Immutability**: Prefer immutable objects where possible

### Testing Conventions
- **Test Structure**: Follow Arrange-Act-Assert (AAA) pattern
- **Test Libraries**: JUnit 5 + AssertJ for assertions
- **Test Naming**: Descriptive method names explaining the scenario being tested
- **Documentation**: Include JavaDoc comments for test classes explaining purpose
- **Mocking**: Use Mockito for unit tests, real implementations for integration tests

### Code Organization
- **Module Structure**: Each service follows hexagonal architecture modules
- **Dependency Injection**: Use Spring's dependency injection
- **Error Handling**: Clean exception handling (TODO: implement custom API responses)
- **Documentation**: JavaDoc for public APIs and complex business logic

## Frontend Code Style (Vue 3/TypeScript)

### Conventions
- **Components**: PascalCase for component names, kebab-case for file names
- **Composables**: camelCase starting with "use" (e.g., `useGreeting`)
- **Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE
- **Files**: kebab-case or PascalCase for component files, camelCase for utilities
- **Props**: camelCase in script, kebab-case in templates
- **Events**: kebab-case

### Quality Tools
- **Linting**: ESLint with TypeScript and Vue rules
- **Testing**: Vitest with coverage reporting, Vue Test Utils
- **E2E Testing**: Cypress with Cucumber/Gherkin
- **Build**: Vite with TypeScript checking

## General Development Guidelines

### Git Conventions
- Descriptive commit messages
- Feature branching recommended
- CI/CD integration with GitHub Actions

### Quality Standards
- Code coverage tracking with JaCoCo (backend) and Vitest (frontend)
- SonarCloud integration for quality gates
- Parallel test execution enabled for performance

### Documentation
- README with architecture diagrams
- Inline comments for complex business logic
- Test documentation explaining scenarios