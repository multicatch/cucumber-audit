Feature: Step Definitions Test

  Scenario: Protocol Inspection Steps - HTTPS
    When I connect to "https://google.com"
    Then the connection should be secure