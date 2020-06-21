Feature: Authentication Request Threats

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed
    And app under "$heartbeat_url" has already started

  @Tampering
  Scenario: Confidential Data Eavesdropping
  HTTP communication is unencrypted and thus prone to altering via Man-in-The-Middle Attacks.
  HTTPS is recommended so it prevents confidential data eavesdropping (such as passwords in requests)
  and response altering.

    When I connect to "$auth_application_url"
    Then the connection should be secure

  @ElevationOfPrivilege
  Scenario: Hostile Linking
  An attacker can prepare a link that automatically authorizes the application without any user action.

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

