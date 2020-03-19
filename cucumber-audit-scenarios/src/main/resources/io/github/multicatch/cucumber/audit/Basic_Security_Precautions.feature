Feature: Basic Security Precautions

  Background:
    Given only whitelisted traffic is allowed
    And traffic matching "http://localhost\.:8081.*" is allowed

  Scenario: The HttpOnly flag
    The HttpOnly flag in "Set-Cookie" header disables the ability to access the cookie through JavaScript.
    If an attacker successfully performs an XSS attack, the HttpOnly flag prevents from stealing the session cookie.

    Given the response headers are under inspection
    When I go to "http://localhost.:8081"
    Then a "Set-Cookie" response header should contain "HttpOnly"