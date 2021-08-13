Feature: Creation of a Greeting Message

  Rule: There are three type of Greeting, Birthday, Anniversary and Christmas

    @e2e
    Scenario: Simple Greeting
      When I create an anniversary greeting for Charles
      Then I get the message "Joyful Anniversary Charles !"
      Then a Greeting is created

    Scenario Template: creation
      When I create a <type> greeting for <name>
      Then I get the message "<message>"
      Scenarios:
        | type      | name   | message                  |
        | birthday  | Anna   | Happy Birthday Anna !    |
        | christmas | Leslie | Merry Christmas Leslie ! |

  Rule: Creation of a none existing type of greeting should return an error

    Scenario: Error
      When I create a birth greeting for Bob
      Then I get an error