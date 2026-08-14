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

            /**
             * Custom command to open the stats page with a known set of counters
             * @example cy.visitStatsWithCounters({BIRTHDAY: 1})
             */
            visitStatsWithCounters(counters: Record<string, number>): Chainable<void>;
        }
    }
}

// The only types the UI offers in its dropdown.
const KNOWN_TYPES = ['BIRTHDAY', 'ANNIVERSARY', 'CHRISTMAS'];

// Match a dropdown option on its whole label. A substring match would let
// 'BIRTH' select the 'BIRTHDAY' option and silently succeed.
const exactOption = (label: string) => new RegExp(`^\\s*${label}\\s*$`);

// Create a greeting
Cypress.Commands.add('createGreeting', (type: string, name: string) => {
    const wanted = type.toUpperCase();
    const isKnownType = KNOWN_TYPES.includes(wanted);

    cy.visit('http://localhost:5173/');

    if (!isKnownType) {
        // The dropdown cannot offer a type that does not exist, so the only way the
        // UI meets one is when the API rejects it. Reproduce the backend's response
        // for an unknown type (HTTP 500) and assert how the UI reports it.
        cy.intercept('POST', '/api/greetings', {
            statusCode: 500,
            body: {status: 500, error: 'Internal Server Error'},
        }).as('createGreetingRequest');
    }

    // Handle PrimeVue Select component
    cy.get('[data-cy=greeting-type]').click();
    cy.contains('li', exactOption(isKnownType ? wanted : 'BIRTHDAY')).click();
    cy.get('[data-cy=greeting-name]').type(name);
    cy.get('[data-cy=create-greeting]').click();

    if (!isKnownType) {
        cy.wait('@createGreetingRequest');
    }
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
    cy.contains('li', exactOption(type.toUpperCase())).click();
    cy.get('[data-cy=update-greeting]').click();
});

// Show the statistics page with a known set of counters.
// The counting itself is covered by the service-level BDD suites; here we only
// assert that the UI renders what the stats API returns, which keeps these
// scenarios independent of whatever the shared database already holds.
Cypress.Commands.add('visitStatsWithCounters', (counters: Record<string, number>) => {
    cy.intercept('GET', '/api/stats', {statusCode: 200, body: {counters}}).as('statsRequest');
    cy.visit('http://localhost:5173/stats');
    cy.wait('@statsRequest');
});

// Verify error message
Cypress.Commands.add('verifyErrorMessage', () => {
    cy.get('[data-cy=error-message]').should('be.visible');
});

export {};
