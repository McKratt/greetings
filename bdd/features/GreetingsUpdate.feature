Feature: Update an existing Greeting

  Rule: You can only update an anniversary greeting to a birthday one and reverse
    Scenario Template: update a greeting
      Given an existing <type> greeting
      When I change the type to <updatedType>
      Then the greeting is now a <updatedType> one
      Examples:
        | type        | updatedType |
        | anniversary | birthday    |
        | birthday    | anniversary |

  Rule: The update of a christmas greeting is forbidden
    Scenario Template: Cannot change christmas greeting
      Given an existing christmas greeting
      When I change the type to <type>
      Then I get an error
      Examples:
        | type        |
        | anniversary |
        | birthday    |