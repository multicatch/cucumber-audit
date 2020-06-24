Feature: Authentication Page Threats

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed
    And app running on "$heartbeat_url" has already started in less than 30 s

  @Spoofing
  Scenario: Application Impersonation
  The server certificate assures that the server is not an impersonated agent trying to deceive the user.
  An attacker could use this vulnerability to steal sensitive data from the user or make them transfer money.

    When I connect to "$auth_application_url"
    Then the connection should be secure

  @Spoofing
  Scenario: Session Hijacking Through XSS
  During an XSS attack, the session cookie could be stolen if it's accessible through JavaScript.
  The attacker then could gain access to the user session and use their account to authorize in other applications.

    Given the response headers are under inspection
    And cookies are cleared
    When I go to "$auth_application_url"
    Then the "Set-Cookie" response header should contain "HttpOnly"

  @InformationDisclosure
  Scenario: Exploitation of System Software Information In Headers
  The disclosure of software information may be used to provide knowledge about known vulnerabilities of
  a particular version. The "Server" and "X-Powered-By" headers provide information about technology that
  is used on the server side. Disabling them makes it more difficult to exploit the server software.

    Given the response headers are under inspection
    When I go to "$auth_application_url"
    Then the "Server" response header should not contain numbers
    And the "X-Powered-By" response header should not contain numbers

  @InformationDisclosure
  Scenario Outline: Exploitation of Sensitive Information on Error Pages
  The disclosure of software information may be used to provide knowledge about known vulnerabilities of
  a particular version. The default error pages can contain information about the server software.
  Overriding default error pages makes it more difficult to exploit the server software.

    Given the response content is under inspection
    When I go to "$auth_application_url/shouldbenotfound"
    Then the response should not contain "<software>"

    Examples:
      | software    |
      | nginx       |
      | Apache      |
      | ASP.NET     |
      | Django      |
      | HTTP Server |

  @InformationDisclosure
  Scenario: Exploitation of System Architecture Information On Error Pages
  Usually the server software prints stack traces on error by default. This is a debug feature that should be
  disabled when running the software in production. The stack trace may provide information about architecture
  and used libraries that can be used by an attacker to exploit known vulnerabilities.

    Given the response content is under inspection
    When I use a "POST" HTTP method
    And I add a "Authentication" header with value "!Unsupported"
    And I add a "Content-Type" header with value "application/unsupported"
    And I make a request to "$auth_application_url/?error=1283927"
    Then the response should not contain "Exception"
    And the response should not contain "Stacktrace"
    And the response should not contain "Traceback"
    And the response should not match ".*[a-zA-Z0-9.]+:[0-9]+\).*"
    And the response should not match "(?i).*line [0-9]+.*"
    And the response should not match "(?i).*debug.*"

  @Tampering
  Scenario: User Deception with Modified Responses
  If the communication is unencrypted, there is a risk that an attacker could use a Man-in-The-Middle attack
  to modify responses. They may use a modified page to make user think they authorize a different application.

    When I connect to "$auth_application_url"
    Then the connection should be secure