Feature: Security Misconfiguration

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "http://localhost\.:8080.*" is allowed

  Scenario: The HttpOnly flag
    The HttpOnly flag in "Set-Cookie" header disables the ability to access the cookie through JavaScript.
    If an attacker successfully performs an XSS attack, the HttpOnly flag prevents from stealing the session cookie.

    Given the response headers are under inspection
    When I go to "http://localhost.:8080"
    Then the "Set-Cookie" response header should contain "HttpOnly"

  Scenario: Headers indicating server software
    The "Server" and "X-Powered-By" headers provide information about technology that is used on the server side.
    They usually contain the software version (eg. "Apache/2.2.15 (CentOS) ...") and can be used to find
    known vulnerabilities of that software. Disabling them makes it more difficult to exploit the server software.

    Given the response headers are under inspection
    When I go to "http://localhost.:8080"
    Then the "Server" response header should not contain numbers
    And the "X-Powered-By" response header should not contain numbers