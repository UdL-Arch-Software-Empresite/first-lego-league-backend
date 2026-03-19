Feature: Edition Team Registration

  Background:
    Given I login as "admin" with password "password"

  Scenario: Register a team successfully
    Given an edition with id 1 exists
    And a team with name "Team 5" exists
    When I register team "Team 5" in edition 1
    Then the response status should be 201 Created

  Scenario: Register the same team twice returns conflict
    Given an edition with id 1 exists
    And a team with name "Team 5" exists
    And team "Team 5" is already registered in edition 1
    When I register team "Team 5" in edition 1 again
    Then the response status should be 409 Conflict

  Scenario: List all teams registered in an edition
    Given an edition with id 1 exists
    And team "Team 5" is registered in edition 1
    And team "Team 6" is registered in edition 1
    When I request the list of teams for edition 1
    Then the response status should be 200 OK