import {Then} from "@badeball/cypress-cucumber-preprocessor";

// Steps shared by several features. The preprocessor resolves step definitions
// per feature file, so anything used by more than one feature lives here.

Then('I get an error', () => {
    cy.verifyErrorMessage();
});
