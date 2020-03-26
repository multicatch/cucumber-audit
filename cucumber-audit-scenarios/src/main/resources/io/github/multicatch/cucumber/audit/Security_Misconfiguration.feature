Feature: Security Misconfiguration

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "$allowed_traffic_regex" is allowed

  Scenario: The HttpOnly flag
  The HttpOnly flag in "Set-Cookie" header disables the ability to access the cookie through JavaScript.
  If an attacker successfully performs an XSS attack, the HttpOnly flag prevents from stealing the session cookie.

    Given the response headers are under inspection
    When I go to "$auth_server_base_url"
    Then the "Set-Cookie" response header should contain "HttpOnly"

  Scenario: Headers indicating server software
  The "Server" and "X-Powered-By" headers provide information about technology that is used on the server side.
  They usually contain the software version (eg. "Apache/2.2.15 (CentOS) ...") and can be used to find
  known vulnerabilities of that software. Disabling them makes it more difficult to exploit the server software.

    Given the response headers are under inspection
    When I go to "$auth_server_base_url"
    Then the "Server" response header should not contain numbers
    And the "X-Powered-By" response header should not contain numbers

  Scenario Outline: Server software on error pages
  The default error pages can contain information about the server software. Usually this includes the version
  of the software used. This piece of information can be used to find known vulnerabilities of that software.
  Overriding default error pages makes it more difficult to exploit the server software.

    Given the response content is under inspection
    When I go to "$auth_server_base_url/shouldbenotfound"
    Then the response should not contain "<software>"

    Examples:
      | software    |
      | nginx       |
      | Apache      |
      | ASP.NET     |
      | HTTP Server |

  Scenario: Stack traces on error pages
  Usually the server software prints stack traces on error by default. This is a debug feature that should be disabled
  when running the software in production. The stack trace may provide information that can be used by an attacker,
  eg. used libraries, algorithms or server software.

    Given the response content is under inspection
    When I make a "PATCH" request to "$auth_server_base_url"
    Then the response should not contain "Exception"
    And the response should not contain "Stacktrace"
    And the response should not contain "Traceback"
    And the response should not match "[a-zA-Z0-9.]+:[0-9]+\)"
    And the response should not match "(l|L)ine [0-9]+"