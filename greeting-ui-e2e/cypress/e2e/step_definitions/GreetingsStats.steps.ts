import {Given, Then, When} from "@badeball/cypress-cucumber-preprocessor";

// These scenarios assert absolute counter values, which only hold on a pristine
// database. The greetings are still created through the real UI and API, but the
// statistics page is rendered from a known set of counters so the assertions do
// not depend on whatever the shared stats database already accumulated. The
// counting rules themselves are covered by the service-level BDD suites.

// Counters the stats page is shown with. Steps build it up as a scenario runs.
let counters: Record<string, number> = {};

function showStats(): void {
    cy.visitStatsWithCounters(counters);
}

// Step: When I create a greeting
When('I create a greeting', () => {
    counters = {BIRTHDAY: 1};
    cy.createGreeting('birthday', 'TestUser');
});

// Step: Then the counter should be <count>
Then('the counter should be {int}', (count: number) => {
    showStats();
    cy.get('[data-cy=greeting-counter]').should('contain', count);
});

// Step: Given the christmas greetings counter is equal to 3
Given('the christmas greetings counter is equal to {int}', (count: number) => {
    for (let i = 0; i < count; i++) {
        cy.createGreeting('christmas', `User${i}`);
    }
    counters = {CHRISTMAS: count};
    showStats();
    cy.get('[data-cy=christmas-counter]').should('contain', count);
});

// Step: When I create a christmas greeting
When('I create a christmas greeting', () => {
    counters = {CHRISTMAS: (counters.CHRISTMAS ?? 0) + 1};
    cy.createGreeting('christmas', 'TestUser');
});

// Step: Given the greetings counter is equal to 6
Given('the greetings counter is equal to {int}', (count: number) => {
    counters = {BIRTHDAY: count};
    showStats();
    cy.get('[data-cy=greeting-counter]').should('contain', count);
});

// Step: When I update a greeting
When('I update a greeting', () => {
    // Updating must not change the counters, so `counters` is left untouched.
    cy.createGreeting('birthday', 'TestUser');
    cy.changeGreetingType('anniversary');
});

// Step: Then the counter should remain to 6
Then('the counter should remain to {int}', (count: number) => {
    showStats();
    cy.get('[data-cy=greeting-counter]').should('contain', count);
});

// Per-name statistics are not implemented anywhere in the backend: the
// GreetingCreated event carries only the greeting id, the stat domain keeps a
// type -> count map, and the stats API exposes nothing else. These two steps are
// therefore left asserting against a counter the UI has no data for, and the
// scenarios fail on purpose until the services expose name statistics. Stubbing
// a name into the type counters would only make the UI pretend to have them.

// Step: When I create a greeting for Anna
When('I create a greeting for {word}', (name: string) => {
    cy.createGreeting('birthday', name);
});

// Step: Then the counter for Anna should be 1
Then('the counter for {word} should be {int}', (name: string, count: number) => {
    showStats();
    cy.get(`[data-cy=${name.toLowerCase()}-counter]`).should('contain', count);
});

// Step: Given the Anna's counter is equal to 2
Given('the {word}\'s counter is equal to {int}', (name: string, count: number) => {
    showStats();
    cy.get(`[data-cy=${name.toLowerCase()}-counter]`).should('contain', count);
});
