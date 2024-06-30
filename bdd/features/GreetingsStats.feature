@stat
Feature: Statistics

  Rule: Each created greeting is counted

    @e2e
    Scenario: First greeting
      When I create a greeting
      Then the counter should be 1

#    @e2e
    Scenario: More greetings
      Given the christmas greetings counter is equal to 3
      When I create a christmas greeting
      Then the counter should be 4
#
# TODO finish steps
#  Rule: Each updated greeting should not be counted
#
#    Scenario: Update a greeting
#      Given the greetings counter is equal to 6
#      When I update a greeting
#      Then the counter should remain to 6
#
#  Rule: We keep stat about name, how many time a name is used in greetings
#
#    Scenario: First time used
#      When I create a greeting for Anna
#      Then the counter for Anna should be 1
#
#    Scenario: More Anna
#      Given the Anna's counter is equal to 2
#      When I create a greeting for Anna
#      Then the counter for Anna should be 3