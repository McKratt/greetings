Feature: Creation of a Greeting Message

  Rule: There are three type of Greeting, Birthday, Anniversary and Christmas
    Scenario Template: creation
      When I create a <type> greeting for <name>
      Then I get the message "<message>"
      Examples:
        | type        | name    | message                      |
        | birthday    | Anna    | Happy Birthday Anna !        |
        | anniversary | Charles | Joyful Anniversary Charles ! |
        | christmas   | Leslie  | Merry Christmas Leslie !     |

  Rule: Creation of a none existing type of greeting should return an error
    Scenario: Error
      When I create a birth greeting for Bob
      Then I get an error