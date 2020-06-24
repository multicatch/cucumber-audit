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
    And the "Content-Type" response header should match ".*json"
    And the "Content-Type" response header should not contain numbers
    And the "Content-Type" response header should not contain "SHOULD NOT BE PRESENT"
    And the "Content-Type" response header should not match "[0-9]+"
    And the response code should not be 404
    And the response should not contain "SHOULD NOT BE PRESENT"
    And the response should not match "SHOULD NOT MATCH"
    And the response should contain "httpbin.org"
    And the response should match ".*httpbin\.org.*"

  Scenario: Document Inspection Steps
    When I go to "https://httpbin.org/"
    Then there should be element selected by "body" on the page
    And there should be element selected by "//html/body"
    And the document should contain "A simple HTTP Request &amp; Response Service."
    And the document should not contain "SHOULD NOT MATCH"
    And the document should match ".*body.*"
    And the document should not match "SHOULD NOT MATCH"