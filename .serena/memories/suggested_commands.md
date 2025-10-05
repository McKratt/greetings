# Suggested Commands for Greetings Project

## Backend Services (Maven)

### Building
```bash
# Build all services from parent directory
cd greetings-parent && mvn clean install

# Build specific service
cd greetings-service && mvn clean compile
cd greetings-stat-service && mvn clean compile

# Skip tests during build
mvn clean install -DskipTests

# Build with coverage reporting
mvn clean install -Pcoverage
```

### Testing
```bash
# Run unit tests
mvn test

# Run unit tests with coverage
mvn -Pcoverage test

# Run integration tests only
mvn verify -DskipUTs

# Run integration tests with coverage
mvn -Pcoverage verify -DskipUTs

# Run specific test class
mvn test -Dtest=SimpleGreetingTest

# Run BDD tests
mvn verify -Dcucumber.filter.tags="@e2e"

# Run contract tests (Pact)
mvn test -Dtest=*Pact*
```

### Docker
```bash
# Build Docker image for Greetings Service
cd greetings-service && mvn clean package -PbuildImage -DskipTests

# Build Docker image for Stats Service
cd greetings-stat-service && mvn clean package -PbuildImage -DskipTests
```

## Frontend (yarn)

### Building & Running
```bash
cd greetings-ui

# Install dependencies
yarn install

# Start development server
yarn run start

# Build for production
yarn run build

# Build and watch for changes
yarn run watch

### Testing
# Run tests
yarn test

# Run Pact tests
yarn run pact

# Run linting
yarn run lint
```

## System Commands (macOS/Darwin)
```bash
# Find files
find . -name "*.java" -type f

# Search in files
grep -r "pattern" .

# List directories
ls -la

# Change directory
cd path/to/directory

# Git operations
git status
git add .
git commit -m "message"
git push
```

## Quality & Analysis
```bash
# SonarCloud analysis (run after tests)
mvn -Pcoverage org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dmaven.test.skip
```