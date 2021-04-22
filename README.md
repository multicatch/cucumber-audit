# cucumber-audit

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=multicatch_cucumber-audit&metric=alert_status)](https://sonarcloud.io/dashboard?id=multicatch_cucumber-audit)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=multicatch_cucumber-audit&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=multicatch_cucumber-audit)

Easy automated auditing with preconfigured step definitions.

This project aims to provide an easy way for configuring an automated audit or web application test using Selenium and Cucumber.

## Features

* write audit scenarios using natural language (Gherkin) thanks to [Cucumber](https://github.com/cucumber/cucumber)
* explore your application and inspect responses thanks to [Selenium](https://github.com/SeleniumHQ/selenium)
* use prepared step definitions for writing scenarios without Java/Kotlin knowledge
* use variable substitution in feature files
* manipulate or inspect HTTP requests and responses thanks to [browsermob-proxy](https://github.com/lightbody/browsermob-proxy)

## Full Documentation

[Available here](doc/README.md)

## Sample Scenario

By using predefined steps from this project, you can build a feature like the following:

```gherkin
Feature: Example Feature

  Scenario: An Expected Response
    Given app running on "https://httpbin.org" has already started
    And the response content is under inspection
    When I go to "https://httpbin.org"
    Then the response should contain "A simple HTTP Request &amp; Response Service."
```

Feel free to extend it with more steps, comments or annotations:

```gherkin
  @InformationDisclosure
  Scenario: Exploitation of System Software Information In Headers
  The disclosure of software information may be used to provide knowledge about known vulnerabilities of
  a particular version. The "Server" and "X-Powered-By" headers provide information about technology that
  is used on the server side. Disabling them makes it more difficult to exploit the server software.

    Given the response headers are under inspection
    When I go to "$auth_application_url"
    Then the "Server" response header should not contain numbers
    And the "X-Powered-By" response header should not contain numbers
```

## Running Sample Scenarios

The scenarios are available [here](cucumber-audit-scenarios/src/main/resources/io/github/multicatch/cucumber/audit).

Download a Selenium Gecko driver and run the following command in the project directory:

```shell script
mvn test -DfailIfNoTests=false -Dtest=CucumberTest -Dwebdriver.gecko.driver=/path/to/geckodriver
```

## Running It Standalone

You can run it standalone using jar build in cucumber-audit-standalone module.

Example:

```shell script
java -jar cucumber-audit-standalone.jar path/to/features \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --webdriver.type GECKO \
  --webdriver.gecko.driver path/to/geckodriver \
  --webdriver.headless true
```

Instead of `path/to/features` provide a path to your `*.feature` files. 

Other options:
* `webdriver.type` - specify type of the webdriver used (GECKO/CHROME)
* `webdriver.gecko.driver` - specify location of the geckodriver binary
* `webdriver.chrome.driver` - specify location of the chromedriver binary
* `webdriver.headless` - specify whether to start browser in the headless or not (true/false)

You can also use options available in cucumber standalone. To see them use `--help`.

To use variable substitution plugin, use `--plugin io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor` option.
Variables will be read from the environment.

## Pretty reports

This project uses [`de.monochromata.cucumber:reporting-plugin`](https://gitlab.com/monochromata-de/cucumber-reporting-plugin) to generate pretty reports.

By default, pretty html reports are generated into `cucumber-audit-scenarios/target/cucumber/cucumber-html-reports` directory.

Reports are automatically deployed and available here:
* [master](https://multicatch.github.io/cucumber-audit/master/cucumber-html-reports/overview-features.html)
* [develop](https://multicatch.github.io/cucumber-audit/develop/cucumber-html-reports/overview-features.html)
