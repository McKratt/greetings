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
    // The UI displays the type as the backend stores it (upper case), while the
    // feature file spells it in lower case. Case is not part of the specification.
    cy.get('[data-cy=greeting-type-display]')
        .invoke('text')
        .should((text: string) => {
            expect(text.trim().toUpperCase()).to.contain(updatedType.toUpperCase());
        });
});
