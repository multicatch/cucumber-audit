Feature: Authentication Request Threats

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed
    And app running on "$heartbeat_url" has already started

  @InformationDisclosure
  Scenario: Confidential Data Eavesdropping
  HTTP communication is unencrypted and thus prone to eavesdropping via Man-in-The-Middle Attacks.
  HTTPS is recommended for safe exchange of confidential information such as passwords or tokens.

    When I connect to "$auth_application_url"
    Then the connection should be secure

  @ElevationOfPrivilege
  Scenario: Hostile Linking
  An authorization granted to the application should not be a stateless mechanism.
  An attacker can prepare a link that automatically authorizes the application without any user action.
  The authorization should only be valid when done by the user.

    Given I go to "$client_application_url"
    And I click on "a.btn-primary"
    And I enter "demo" into a field selected by "#id_username"
    And I enter "easypassword" into a field selected by "#id_password"
    And I click on "input[type=submit]"
    And the response headers are under inspection
    When I use a "POST" HTTP method
    And I add a "Content-Type" header with value "application/x-www-form-urlencoded"
    And the request body is "csrfmiddlewaretoken=wrong_csrf&redirect_uri=http%3A%2F%2Fhttpbin.org%2F&scope=read+write&client_id=$client_id&state=4564382&response_type=code&code_challenge=&code_challenge_method=&allow=Authorize"
    And I make a request to "$authorization_url/?response_type=code&state=4564382&client_id=$client_id"
    Then the response code should be 403

