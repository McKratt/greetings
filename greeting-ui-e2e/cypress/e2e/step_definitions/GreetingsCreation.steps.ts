import {Then, When} from "@badeball/cypress-cucumber-preprocessor";

When('I create a(n) {word} greeting for {word}', (type: string, name: string) => {
    cy.createGreeting(type, name);
});


Then('I get the message {string}', (message: string) => {
    cy.verifyGreetingMessage(message);
});

Then('a Greeting is created', () => {
    cy.verifyGreetingCreated();
});

Then('I get an error', () => {
    cy.verifyErrorMessage();
});