Feature: Database Queries Threats

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed
    And app under "$heartbeat_url" has already started

  Scenario: Known Database Vulnerabilities Disclosure
  Unhandled database errors may lead to disclosure about database system version.
  This may be used to prepare an attack that uses known system vulnerabilities.

    Given I am on "$auth_application_url"
    And the response content is under inspection
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

  Scenario Outline: Unsafe Query Handling
  Creation of database queries by simply inserting parameters into a string pattern is an SQL Injection
  vulnerability. An attacker can use form submit to access or alter information present in the database.

    Given I am on "$auth_application_url"
    And the response content is under inspection
    When I enter "<unsafe_form_value>" into a field selected by "input[name='username']"
    And I enter "<unsafe_form_value>" into a field selected by "input[name='password']"
    And I click on "form input[type='submit']"
    Then the response time should not be longer than 5000 ms

    Examples:
      | unsafe_form_value                               |
      | ' OR sleep(5000) -- .                           |
      | ' \|\| dbms_pipe.receive_message(('a'), 5) -- . |
      | ' OR pg_sleep(5) -- .                           |
      | ' OR WAITFOR DELAY '0:0:05' -- .                |