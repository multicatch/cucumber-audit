# Predefined Steps

## Using Predefined Steps

The predefined steps are added to the standalone jar by default,
but are optional. The `--glue io.github.multicatch.cucumber.audit` option enables them.

To use the predefined steps in a Java Project, add the following Maven dependency:

```xml
        <dependency>
            <groupId>io.github.multicatch</groupId>
            <artifactId>cucumber-audit-stepdefs</artifactId>
            <version>${cucumber.audit.version}</version>
            <scope>test</scope>
        </dependency>
```

The predefined steps need an injected `AuditContext`. 
To get more info how to configure Dependency Injection in tests, see [Getting Started](GettingStarted.md).

## Steps Provided With cucumber-audit

### Diagnostic Steps

`app running on {string} has already started`

This steps tries to establish an HTTP connection with a given address and sends a GET request.
The test will be stopped until a `200 OK` response is received. 

`app running on {string} has already started in less than {int} s`

This step is similar to the above, but fails if the connection has not been established in a given time.

### Whitelist Steps

`only whitelisted traffic is allowed`

This step enables whitelisted traffic in an embedded proxy. 
Use this step if you want to filter traffic for more accurate response testing.

`traffic matching {string} is allowed`

This step adds a rule to a whitelist in an embedded proxy. 
This steps needs an activated traffic filter, so it needs to be run after `only whitelisted traffic is allowed`.

### Protocol Inspection Steps

`I connect to {string}`

Connects to the given URL *without* using the browser.

`the connection should be secure`

Checks whether the connection has been established with an HTTPS connection.

### Navigation Steps

`cookies are cleared`

Clears all cookies in a browser. Useful when there is a need to start a new session.

`I go to {string}`

Opens a given address in browser. 

`I am on {string}`

The same as the above; mainly for use in **Given** stage.

`I use a {string} HTTP method`

Changes the HTTP method of the next browser request. Note: the method will be reset after the scenario.

`I add a {string} header with value {string}`

Sets the value of a given HTTP header of the next browser request. Note: custom headers will be reset after the scenario.

`the request body is {string}`

Sets the request body of next HTTP request to a given value. Note: this setting will be reset after the scenario.

`I make a request to {string}`

Makes a request with given HTTP method, headers and body in a browser, and then resets those settings.

### Response Inspection Steps

`the response headers are under inspection`

Enables capture of response headers in the embedded proxy and resets captured data.

**Notice** The following steps need this option enabled to work.

`the {string} response header should contain {string}`

Checks whether a response header of a given name contains a string. Notice: This step will succeed when at least one occurrence in all captured responses is found.

`the {string} response header should not contain {string}`

Checks whether a response header of a given name does not contain a string. Notice: This step will fail when at least one occurrence in all captured responses is found.

`the {string} response header should not contain numbers`

Checks whether a response header of a given name does not contain any numbers. Notice: This step will fail when at least one occurrence in all captured responses is found.

`the {string} response header should match {string}`

Checks whether a response header of a given name matches a given regular expression (Java style). Notice: This step will succeed when at least one match in all captured responses is found.

`the {string} response header should not match {string}`

Checks whether a response header of a given name does not match a given regular expression (Java style). Notice: This step will fail when at least one match in all captured responses is found.

`the response content is under inspection`

Enables capture of response content in the embedded proxy and resets captured data.

**Notice** The following steps need this option enabled to work.

`the response should contain {string}`

Checks whether a response captured in proxy contains string. Notice: This step will succeed when at least one occurrence in all captured responses is found.

`the response should not contain {string}`

Checks whether a response captured in proxy does not contain string. Notice: This step will fail when at least one occurence in all captured responses is found.

`the response should match {string}`

Checks whether a response captured in proxy matches a regular expression (Java style). Notice: This step will succeed when at least one match in all captured responses is found.

`the response should not match {string}`

Checks whether a response captured in proxy does not match a regular expression (Java style). Notice: This step will fail when at least one match in all captured responses is found.

`the response time should not be longer than {long} ms`

Checks whether a response time is no longer than a given value. Notice: This step will fail when at least one response in all captured responses was slower.

`the response code should be {int}`

Checks whether a response captured in proxy has a given response code. Notice: This step will succeed when at least one match in all captured responses is found.

`the response code should not be {int}`

Checks whether a response captured in proxy has not a given response code. Notice: This step will fail when at least one match in all captured responses is found.

### Document Inspection Steps

`I enter {string} into a field selected by {string}`

Enters a given value into a field selected by a given CSS selector (for example `input[name=login]` or `input#login`).

`I click on {string}`

Selects all elements matching a CSS selector (for example `input[type=submit]` or `input#submit`) and clicks on them.

`there should be element selected by {string} on the page`

Checks if there are any elements matched by a CSS selector (for example `.login-form input[name=login]` or `.login-form input#login`).

`there should be element selected by {string}`

Checks whether an XPath selects any element.

`the document should contain {string}`

Checks whether the source code of a page contains a string. Notice: This tests the response received by the browser, not the one captured by the proxy.

`the document should not contain {string}`

Checks whether the source code of a page does not contain a string. Notice: This tests the response received by the browser, not the one captured by the proxy.

`the document should match {string}`

Checks whether the source code of a page matches a given regular expression (Java style). Notice: This tests the response received by the browser, not the one captured by the proxy.

`the document should not match {string}`

Checks whether the source code of a page does not match a given regular expression (Java style). Notice: This tests the response received by the browser, not the one captured by the proxy.

## Example Scenarios

```gherkin
  @InformationDisclosure
  Scenario: System Architecture Disclosure (Error Pages)
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
```

[More available here](../cucumber-audit-scenarios/src/main/resources/io/github/multicatch/cucumber/audit)
