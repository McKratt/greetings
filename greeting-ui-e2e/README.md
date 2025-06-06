test# Greeting UI End-to-End Tests

This directory contains end-to-end tests for the Greetings UI application using Cypress and Cucumber.

## Overview

The tests are written using:

- [Cypress](https://www.cypress.io/) - A JavaScript end-to-end testing framework
- [TypeScript](https://www.typescriptlang.org/) - For type safety and better developer experience
- [Cucumber](https://cucumber.io/) - For BDD-style tests using Gherkin syntax

The tests use the feature files from the `../bdd/features` directory and implement step definitions in the
`cypress/e2e/step_definitions` directory.

## Project Structure

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

## Prerequisites

- Node.js 18+
- Yarn

## Installation

```bash
# Install dependencies
yarn install
```

## Running Tests

The tests can be run in two modes:

### Headless Mode

This will start the UI application automatically, run the tests, and then shut down the UI application:

```bash
yarn test
```

### Interactive Mode

This will start the UI application automatically and open the Cypress UI, allowing you to run tests interactively:

```bash
yarn test:open
```

### Running Tests Against an Already Running UI Application

If you already have the UI application running (e.g., with `cd ../greetings-ui && yarn dev`), you can run the tests
directly:

```bash
# Run tests in headless mode
yarn cypress:run

# Run tests in interactive mode
yarn cypress:open
```

## Writing Tests

### Feature Files

The feature files are located in the `../bdd/features` directory and use Gherkin syntax. For example:

```gherkin
Feature: Creation of a Greeting Message

  Rule: There are three type of Greeting, Birthday, Anniversary and Christmas

    Scenario: Simple Greeting
      When I create an anniversary greeting for Charles
      Then I get the message "Joyful Anniversary Charles !"
      Then a Greeting is created
```

### Step Definitions

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

### Custom Commands

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

## Best Practices

1. **Use TypeScript**: TypeScript provides better type safety and developer experience.
2. **Use Custom Commands**: Encapsulate common actions and assertions in custom commands.
3. **Use Data Attributes**: Use `data-cy` attributes to select elements in the UI.
4. **Keep Step Definitions Simple**: Step definitions should be simple and focused on a single action or assertion.
5. **Use Before and After Hooks**: Use hooks to set up and clean up test state.
6. **Use Aliases**: Use Cypress aliases to store and retrieve values between steps.
7. **Use Fixtures**: Use fixtures to provide test data.
8. **Use Screenshots and Videos**: Enable screenshots and videos to help debug test failures.
9. **Use Retry Ability**: Use Cypress's retry ability to handle asynchronous operations.
10. **Use Cypress Best Practices**: Follow
    the [Cypress Best Practices](https://docs.cypress.io/guides/references/best-practices).