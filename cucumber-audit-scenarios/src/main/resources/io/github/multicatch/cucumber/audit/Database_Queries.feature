Feature: Database Queries Threats

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed
    And app under "$heartbeat_url" has already started

  Scenario: Known Database Vulnerabilities Disclosure
  Creation of database queries by simply inserting parameters into a string pattern is an SQL Injection
  vulnerability. An attacker can use form submit to access or alter information present in the database.

    Given the response content is under inspection
    And I am on "$auth_application_url"
    When I enter "' UNKNOWN SYNTAX; -- ." into a field selected by "input[name='username']"
    And I enter "' UNKNOWN SYNTAX; -- ." into a field selected by "input[name='password']"
    And I click on "form input[type='submit']"
    Then the response should not contain "SQL"
    And the response should not contain "Exception"
    And the response should not contain "Stacktrace"
    And the response should not contain "Traceback"
    And the response should not match "[a-zA-Z0-9.]+:[0-9]+\)"
    And the response should not match "(?i)line [0-9]+"
    And the response should not match "(?i)debug"