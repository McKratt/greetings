// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

// Declare global Cypress namespace to add custom commands
declare global {
    namespace Cypress {
        interface Chainable {
            /**
             * Custom command to create a greeting
             * @example cy.createGreeting('birthday', 'John')
             */
            createGreeting(type: string, name: string): Chainable<void>;

            /**
             * Custom command to verify a greeting message
             * @example cy.verifyGreetingMessage('Happy Birthday John !')
             */
            verifyGreetingMessage(message: string): Chainable<void>;

            /**
             * Custom command to verify a greeting was created
             * @example cy.verifyGreetingCreated()
             */
            verifyGreetingCreated(): Chainable<void>;

            /**
             * Custom command to change greeting type
             * @example cy.changeGreetingType('birthday')
             */
            changeGreetingType(type: string): Chainable<void>;

            /**
             * Custom command to verify error message
             * @example cy.verifyErrorMessage()
             */
            verifyErrorMessage(): Chainable<void>;
        }
    }
}

// Create a greeting
Cypress.Commands.add('createGreeting', (type: string, name: string) => {
    cy.visit('http://localhost:5173/');
    // Handle PrimeVue Select component
    cy.get('[data-cy=greeting-type]').click();
    cy.contains('li', type.toUpperCase()).click();
    cy.get('[data-cy=greeting-name]').type(name);
    cy.get('[data-cy=create-greeting]').click();
});

// Verify greeting message
Cypress.Commands.add('verifyGreetingMessage', (message: string) => {
    cy.get('[data-cy=greeting-message]').should('contain', message);
});

// Verify greeting created
Cypress.Commands.add('verifyGreetingCreated', () => {
    cy.get('[data-cy=greeting-created]').should('be.visible');
});

// Change greeting type
Cypress.Commands.add('changeGreetingType', (type: string) => {
    // Handle PrimeVue Select component
    cy.get('[data-cy=update-greeting-type]').click();
    cy.contains('li', type.toUpperCase()).click();
    cy.get('[data-cy=update-greeting]').click();
});

// Verify error message
Cypress.Commands.add('verifyErrorMessage', () => {
    cy.get('[data-cy=error-message]').should('be.visible');
});

export {};
