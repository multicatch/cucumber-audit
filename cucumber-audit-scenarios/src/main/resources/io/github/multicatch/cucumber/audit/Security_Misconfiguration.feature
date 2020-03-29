Feature: Security Misconfiguration

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed
    And app under "$heartbeat_url" has already started

  Scenario: Use of Plaintext Communication
  HTTP communication is unencrypted and thus prone to altering via Man-in-The-Middle Attacks.
  HTTPS is recommended so it prevents confidential data eavesdropping (such as passwords in requests)
  and response altering.

    When I connect to "$auth_application_url"
    Then the connection should be secure

  Scenario: Cookie Stealing Through XSS
  The HttpOnly flag in "Set-Cookie" header disables the ability to access the cookie through JavaScript.
  If an attacker successfully performs an XSS attack, the HttpOnly flag prevents from stealing the session cookie.

    Given the response headers are under inspection
    And cookies are cleared
    When I go to "$auth_application_url"
    Then the "Set-Cookie" response header should contain "HttpOnly"

  Scenario: Server Software Information Disclosure In Headers
  The "Server" and "X-Powered-By" headers provide information about technology that is used on the server side.
  They usually contain the software version (eg. "Apache/2.2.15 (CentOS) ...") and can be used to find
  known vulnerabilities of that software. Disabling them makes it more difficult to exploit the server software.

    Given the response headers are under inspection
    When I go to "$auth_application_url"
    Then the "Server" response header should not contain numbers
    And the "X-Powered-By" response header should not contain numbers

  Scenario Outline: Server Software Information Disclousure On Error Pages
  The default error pages can contain information about the server software. Usually this includes the version
  of the software used. This piece of information can be used to find known vulnerabilities of that software.
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

  Scenario: Stack Traces On Application Error
  Usually the server software prints stack traces on error by default. This is a debug feature that should be disabled
  when running the software in production. The stack trace may provide information that can be used by an attacker,
  eg. used libraries, algorithms or server software.

    Given the response content is under inspection
    When I use a "POST" HTTP method
    And I add a "Authentication" header with value "!Unsupported"
    And I add a "Content-Type" header with value "application/unsupported"
    And I make a request to "$auth_application_url/?error=1283927"
    Then the response should not contain "Exception"
    And the response should not contain "Stacktrace"
    And the response should not contain "Traceback"
    And the response should not match "[a-zA-Z0-9.]+:[0-9]+\)"
    And the response should not match "(?i)line [0-9]+"
    And the response should not match "(?i)debug"