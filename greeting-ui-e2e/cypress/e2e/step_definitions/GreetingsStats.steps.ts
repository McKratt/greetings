// Step: When I create a greeting

import {Given, Then, When} from "@badeball/cypress-cucumber-preprocessor";

When('I create a greeting', () => {
    cy.createGreeting('birthday', 'TestUser');
});

// Step: Then the counter should be <count>
Then('the counter should be {int}', (count: number) => {
    cy.get('[data-cy=greeting-counter]').should('contain', count);
});

// Step: Given the christmas greetings counter is equal to 3
Given('the christmas greetings counter is equal to {int}', (count: number) => {
    // This is a mock setup - in a real scenario, we might need to set up the database
    // or use API calls to ensure the counter is at the expected value
    cy.log(`Setting up christmas greetings counter to ${count}`);

    // For demonstration purposes, we'll create multiple greetings to reach the count
    // In a real scenario, this might be done through API calls or database setup
    for (let i = 0; i < count; i++) {
        cy.createGreeting('christmas', `User${i}`);
    }

    // Verify the counter is at the expected value
    cy.get('[data-cy=christmas-counter]').should('contain', count);
});

// Step: When I create a christmas greeting
When('I create a christmas greeting', () => {
    cy.createGreeting('christmas', 'TestUser');
});

// The following steps are for the commented-out scenarios in the feature file
// They are included here for completeness but won't be used until those scenarios are uncommented

// Step: Given the greetings counter is equal to 6
Given('the greetings counter is equal to {int}', (count: number) => {
    cy.log(`Setting up greetings counter to ${count}`);
    // Similar to the christmas counter setup
});

// Step: When I update a greeting
When('I update a greeting', () => {
    // First create a greeting
    cy.createGreeting('birthday', 'TestUser');
    // Then update it
    cy.changeGreetingType('anniversary');
});

// Step: Then the counter should remain to 6
Then('the counter should remain to {int}', (count: number) => {
    cy.get('[data-cy=greeting-counter]').should('contain', count);
});

// Step: When I create a greeting for Anna
When('I create a greeting for {word}', (name: string) => {
    cy.createGreeting('birthday', name);
});

// Step: Then the counter for Anna should be 1
Then('the counter for {word} should be {int}', (name: string, count: number) => {
    cy.get(`[data-cy=${name.toLowerCase()}-counter]`).should('contain', count);
});

// Step: Given the Anna's counter is equal to 2
Given('the {word}\'s counter is equal to {int}', (name: string, count: number) => {
    cy.log(`Setting up ${name}'s counter to ${count}`);
    // Similar to the christmas counter setup
});