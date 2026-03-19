Feature: Edition Team Registration

  As a system administrator
  I want to register teams in an edition
  So that teams can participate in the competition

  Scenario: Register a team successfully
    Given an edition with id 1 exists
    And a team with id 5 exists
    When I register team 5 in edition 1
    Then the response status should be 201 Created

  Scenario: Register the same team twice returns conflict
    Given an edition with id 1 exists
    And a team with id 5 exists
    And team 5 is already registered in edition 1
    When I register team 5 in edition 1 again
    Then the response status should be 409 Conflict

  Scenario: List all teams registered in an edition
    Given an edition with id 1 exists
    And team 5 is registered in edition 1
    And team 6 is registered in edition 1
    When I request the list of teams for edition 1
    Then the response status should be 200 OK
    And the response should contain team 5
    And the response should contain team 6