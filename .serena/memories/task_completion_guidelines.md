# Task Completion Guidelines

## When a Development Task is Completed

### 1. Code Quality Checks
```bash
# Run linting (frontend)
cd greetings-ui && yarn lint

# Check code formatting and style compliance
# (Currently no explicit formatter configured - follow established patterns)
```

### 2. Testing Requirements
```bash
# Backend: Run all tests with coverage
cd greetings-service && mvn clean test -Pcoverage
cd greetings-stat-service && mvn clean test -Pcoverage

# Backend: Run integration tests
mvn verify -DskipUTs -Pcoverage

# Frontend: Run tests with coverage
cd greetings-ui && yarn test:coverage

# Frontend: Run tests in watch mode during development
cd greetings-ui && yarn test:watch

# Contract tests (if APIs changed)
mvn test -Dtest=*Pact*
cd greetings-ui && yarn pact
```

### 3. Build Verification
```bash
# Verify all services build successfully
cd greetings-parent && mvn clean install

# Verify frontend builds
cd greetings-ui && yarn build

# Test Docker image creation (if relevant)
mvn clean package -PbuildImage -DskipTests
```

### 4. End-to-End Validation
```bash
# Run BDD tests
mvn verify -Dcucumber.filter.tags="@e2e"

# Run Cypress E2E tests
cd greeting-ui-e2e && yarn test

# Run Cypress E2E tests with UI
cd greeting-ui-e2e && yarn test:open

# Manual verification through REST endpoints if needed
# (Use HttpTests.http or greetings-e2e/src/test/resources/StatsRequests.http)
```

### 5. Documentation Updates
- Update README.md if architecture or setup changes
- Update JavaDoc for new public APIs
- Update inline comments for complex business logic
- Update feature files for BDD tests if behavior changes

### 6. Quality Gates
- Ensure SonarCloud quality gate passes
- Verify code coverage meets project standards
- Check for security vulnerabilities
- Validate performance hasn't degraded

### 7. Git Workflow
```bash
# Commit with descriptive messages
git add .
git commit -m "feat: descriptive message about changes"

# Push and ensure CI/CD pipeline passes
git push
```

## Specific Guidelines by Change Type

### Domain Logic Changes
- Run domain unit tests: `mvn test -pl greetings-domain`
- Verify application layer tests still pass
- Check integration tests for affected modules

### API Changes
- Run contract tests (Pact)
- Update API documentation
- Test with both services if cross-service impact
- Verify UI integration if frontend consumes the API

### Database Changes
- Run persistence layer tests
- Verify Testcontainers integration tests pass
- Check migration scripts if applicable

### Kafka/Messaging Changes
- Test with @EmbeddedKafka integration tests
- Verify producer/consumer contract compatibility
- Run end-to-end message flow tests

### Frontend Changes
- Run Vitest tests with coverage
- Perform linting checks
- Test API integration
- Run Cypress E2E tests if user flows affected
- Verify responsive design if UI changes