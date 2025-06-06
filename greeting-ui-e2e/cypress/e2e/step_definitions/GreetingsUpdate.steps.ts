import {Given, Then, When} from "@badeball/cypress-cucumber-preprocessor";


Given('an existing {word} greeting', (type: string) => {
    // First create a greeting of the specified type
    cy.createGreeting(type, 'TestUser');
    // Verify it was created
    cy.verifyGreetingCreated();
});

// Step: When I change the type to <updatedType>
When('I change the type to {word}', (updatedType: string) => {
    cy.changeGreetingType(updatedType);
});

// Step: Then the greeting is now a <updatedType> one
Then('the greeting is now a {word} one', (updatedType: string) => {
    // Verify the greeting type has been updated
    cy.get('[data-cy=greeting-type-display]').should('contain', updatedType);
});
