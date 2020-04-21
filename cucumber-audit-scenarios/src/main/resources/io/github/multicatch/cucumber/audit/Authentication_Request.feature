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