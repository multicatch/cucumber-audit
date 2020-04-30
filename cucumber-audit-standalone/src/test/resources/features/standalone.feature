Feature: Standalone Test

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "https://httpbin\.org.*" is allowed

  Scenario: Protocol Inspection Steps - HTTPS
    When I connect to "https://httpbin.org/get?branch=$TRAVIS_BRANCH"
    Then the connection should be secure