# Greetings UI

A Vue 3 application for creating and viewing greeting messages. Built with Vue 3, TypeScript, and Vite.

## Features

- Create personalized greeting messages for different event types (Birthday, Christmas, Anniversary)
- View created greeting messages
- Error handling for message retrieval

## Project Setup

```bash
# Install dependencies
yarn install

# Start development server
yarn dev

# Build for production
yarn build

# Preview production build
yarn preview
```

## Running Tests

The project uses Vitest for unit testing. To run the tests:

```bash
# Run tests once
yarn test

# Run tests in watch mode
yarn test:watch
```

### Test Coverage

Tests have been created for the following components:

- **Models**
    - `EventType`: Enum values and structure
    - `Greeting`: Constructor and getters
    - `GreetingMessage`: Constructor and getters

- **Composables**
    - `GreetingsRepository`: Message creation and retrieval

- **Components**
    - `GreetingInput`: Rendering and event emission
    - `GreetingDropdown`: Rendering and event emission

- **Views**
    - `GreetingForm`: Form submission and navigation
    - `Message`: Message display and error handling

## Project Structure

- `src/models`: Data models
- `src/composables`: Shared logic and services
- `src/components`: Reusable UI components
- `src/views`: Page components
- `tests/unit`: Unit tests
