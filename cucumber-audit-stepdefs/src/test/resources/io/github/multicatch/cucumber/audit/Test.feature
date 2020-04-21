Feature: Step Definitions Test

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "https://httpbin\.org.*" is allowed

  Scenario: Protocol Inspection Steps - HTTPS
    When I connect to "https://httpbin.org"
    Then the connection should be secure

  Scenario: Response Inspection Steps
    Given the response headers are under inspection
    And the response content is under inspection
    When I go to "https://httpbin.org/get?id=12"
    Then the "Content-Type" response header should contain "json"
    And the "Content-Type" response header should not contain numbers
    And the response should not contain "SHOULD NOT BE PRESENT"
    And the response should not match "SHOULD NOT MATCH"