# Getting Started

## Environment

To run this tool, you will need JRE 8. [AdoptOpenJDK](https://adoptopenjdk.net/) and [Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html) are supported.

This project is based on Selenium, so you will a driver for a browser of your choice:
* For Firefox, download [geckodriver](https://github.com/mozilla/geckodriver/releases).
* For Chrome/Chromium, download [chromedriver](https://chromedriver.chromium.org/downloads).

A standalone cucumber-audit jar can be downloaded from [releases](https://github.com/multicatch/cucumber-audit/releases).

You can also integrate this tool into an existing project. You will still need a driver for the browser.

## Features and Scenarios

The tests, or rather scenarios, are written in [Gherkin](https://cucumber.io/docs/gherkin/). 
Thanks to it, no programming knowledge is needed to create a custom automated audit.

Multiple scenarios can be saved in a single feature files with a `.feature` extension. There may be more than one 
feature file used.

A sample feature file can look like the following:

```gherkin
Feature: Example Feature

  Scenario: Secure Connection
    Given app running on "https://httpbin.org" has already started
    When I connect to "https://httpbin.org"
    Then the connection should be secure
```

As it can be seen, the file has to start with `Feature: ` line containing the feature name. After that, the scenarios
can be defined.

The scenario has to start with `Scenario: ` and a scenario name. After that, the steps of the scenario can be defined.

The scenario consists of three stages:
* **Given** - a preparation stage, where the state of the environment before an action is defined.
* **When** - the action that is needed to trigger a feature or mechanism in application.
* **Then** - a verification stage, where the result is tested.

Keep in mind that the steps have to be defined in code in order to use them. 
This tool comes with handy predefined steps, which will be discussed later.

In this example, the **Given** stage checks whether the application is actually running. 
If it has not started yet or there is no connection, there is no point in auditing it.

Then, the desired action is the *connection* to the application. 
This **When** step triggers a HTTP connection that will be tested later.

The last step is a **Then** step that makes an assertion that the connection should be secure. 
It actually tests whether the connection is HTTPS and the certificate is trusted.

One stage can contain many steps. They can be joined by using the following words: *And* or *But*.

```gherkin
Feature: Example Feature

  Scenario: An Expected Response
    Given app running on "https://httpbin.org" has already started
    And the response content is under inspection
    When I go to "https://httpbin.org"
    Then the response should contain "A simple HTTP Request &amp; Response Service."
```

We can see that in the **Given** stages there are two steps. 
There were a preparation step added that enables the response content inspection.

**Notice** There is a major difference between *I go to* and *I connect to*. The first one opens the page
in the browser, while the second is used only for protocol inspection.

## Running Features

After defining your first features, you can run them in the command line with cucumber-audit-standalone.

### *NIX

Given you have a gecko driver in `/home/user/geckodriver` and the feature files in a `/home/user/features` directory, 
you can run the following command in a directory containing the `cucumber-audit-standalone.jar`.

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver
```

### Windows

Given you have a gecko driver in `C:\Users\user\geckodriver.exe` and the feature files in a `C:\Users\user\features` directory, 
you can run the following command in a directory containing the `cucumber-audit-standalone.jar`.

```shell script
java.exe -jar cucumber-audit-standalone.jar C:\\Users\\user\\features \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --webdriver.type GECKO \
  --webdriver.gecko.driver C:\\Users\\user\\geckodriver.exe
```

### Using the Chrome Driver

To use a Chrome driver, you should switch `webdriver.type` option to `CHROME`. 
The location of the chrome driver should also be specified with the `webdriver.chrome.driver` option instead of `webdriver.gecko.driver`.

Example: 

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --webdriver.type CHROME \
  --webdriver.chrome.driver /home/user/chromedriver
```

### Sample Output

Suppose you have a feature file named `sample.feature` in `/home/user/features` with the following content:

```gherkin
Feature: Example Feature

  Scenario: An Expected Response
    Given app running on "https://httpbin.org" has already started
    And the response content is under inspection
    When I go to "https://httpbin.org"
    Then the response should contain "A simple HTTP Request &amp; Response Service."
```

To run this feature only with a geckodriver, use the following command:

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features/sample.feature \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver
```

```
Jun 23, 2020 10:54:31 PM org.openqa.selenium.remote.ProtocolHandshake createSession
INFO: Detected dialect: W3C

Scenario: An Expected Response                                                     # sample.feature:3
  Given app running on "https://httpbin.org" has already started                   # io.github.multicatch.cucumber.audit.DiagnosticStepDefs.<init>(DiagnosticStepDefs.kt:14)
  And the response content is under inspection                                     # io.github.multicatch.cucumber.audit.ResponseInspectionStepDefs.<init>(ResponseInspectionStepDefs.kt:19)
  When I go to "https://httpbin.org"                                               # io.github.multicatch.cucumber.audit.NavigationStepDefs.<init>(NavigationStepDefs.kt:28)
  Then the response should contain "A simple HTTP Request &amp; Response Service." # io.github.multicatch.cucumber.audit.ResponseInspectionStepDefs.<init>(ResponseInspectionStepDefs.kt:67)

1 Scenarios (1 passed)
4 Steps (4 passed)
0m4,458s
```

### Silent Mode and Saving The Report

There may be a need to hide the steps in the output of the above command. To do this, just remove `--plugin pretty` from the command.

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features/sample.feature \
  --glue io.github.multicatch.cucumber.audit \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver
```

You can also save the report to a json file by using `--plugin json:report.json`. 
Instead of a `report.json` you can provide a custom file name, where the reports will be saved.

You can use a formatter to display the resulting file in a more readable format. 
There are plenty of tools that convert those reports to HTML files.

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features/sample.feature \
  --glue io.github.multicatch.cucumber.audit \
  --plugin json:report.json \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver
```

The json plugin can be used with a pretty plugin.

### Headless Mode

The browser may need to be run in a headless mode. 
In this mode, the window of the browser is not visible during testing.

To run in in a headless mode, add the `--webdriver.headless true` option.

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver \
  --webdriver.headless true
```

## Integrating It Into A Project

You can also easily integrate it into any Java/Kotlin JVM project. To do this, add the following Maven dependency:

```xml
        <dependency>
            <groupId>io.github.multicatch</groupId>
            <artifactId>cucumber-audit-context</artifactId>
            <version>${cucumber.audit.version}</version>
            <scope>test</scope>
        </dependency>
```

Currently it only works with JUnit 4. You will also need an additional dependency:

```xml
        <dependency>
            <groupId>io.github.multicatch</groupId>
            <artifactId>cucumber-audit-junit</artifactId>
            <version>${cucumber.audit.version}</version>
            <scope>test</scope>
        </dependency>
```

You can then add a class that will run your features:

```java
@RunWith(CucumberAudit.class)
@CucumberOptions(plugin = {
    "pretty"
}, strict = true)
class CucumberAuditTest {
}
```

By default, Cucumber will look for features and step definitions in the same package as the configuration class.
Suppose you have defined the class in a package named `com.example.audit`. 
Cucumber will look for step definitions in `com.example.audit` package, and will look for feature files in
`com/example/audit` folder in your classpath (resources).

To configure the locations of features and step definitions, use `features` and `glue` fields in the `@CucumberOptions` annotation.

You can create an `AuditContext` instance to use a browser driver and manage proxy settings. 
To do so, use the following method:

```
AuditContext context = AuditContextKt.auditContextOf(DriverType.GECKO, "location/of/driver", true);
```

The `AuditContextKt.auditContextOf(DriverType, String, boolean)` method is used to create a class that
will manage the browser driver and the proxy. The first argument is the type of the browser driver,
the second argument is the location of the driver, and the third argument is used to specify whether to run
the browser in a headless mode.

Remember to define a teardown method that will stop the driver and kill the browser after the tests are done!

```java
@RunWith(CucumberAudit.class)
@CucumberOptions(
    plugin = {
        "pretty"
    }, 
    glue = {
        "io.github.multicatch.cucumber.audit"
    },
    strict = true
)
class CucumberAuditTest {
    static AuditContext context = AuditContextKt.auditContextOf(DriverType.GECKO, "location/of/driver", true);

    @AfterClass
    public static void teardown() {
        context.driver.quit();
    }
}
```

### Using Predefined Steps

To use predefined steps in your project, use the following Maven dependency:

```xml
        <dependency>
            <groupId>io.github.multicatch</groupId>
            <artifactId>cucumber-audit-stepdefs</artifactId>
            <version>${cucumber.audit.version}</version>
            <scope>test</scope>
        </dependency>
```

This will add standard cucumber-audit step definitions to your classpath. 
You will need to specify the location of those definitions in a `glue` option in `@CucumberOptions`:

```java
@RunWith(CucumberAudit.class)
@CucumberOptions(
    plugin = {
        "pretty"
    }, 
    glue = {
        "io.github.multicatch.cucumber.audit"
    },
    strict = true
)
class CucumberAuditTest {
}
```

**Notice** These steps need an injected *AuditContext*. You will need to configure a dependency injection
framework (like Guice) with Cucumber. See the [Cucumber documentation](https://cucumber.io/docs/cucumber/state/) for more info.

To configure Guice in your tests, first add the following dependencies:
```xml
        <dependency>
            <groupId>com.google.inject</groupId>
            <artifactId>guice</artifactId>
            <version>4.2.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-guice</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
```

Then define the following configuration classes:
```java
/**
 * This class is used to configure Guice injection in Cucumber tests
 */
class GuiceInjectorSource implements InjectorSource {
    @Override
    public Injector getInjector() {
        return Guice.createInjector(CucumberModules.createScenarioModule(), new CucumberTestModule());
    }
}

/**
 * This class is used to configure dependencies in Guice
 */
class CucumberTestModule extends AbstractModule {
    public static final AuditContext AUDIT_CONTEXT = AuditContextKt.auditContextOf(DriverType.GECKO, "location/of/driver", true);

    @Override
    protected void configure() {
        binder().bind(AuditContext.class).toInstance(AUDIT_CONTEXT);
    }
}
```

In your `cucumber.properties` file you will need to specify the defined injector source:

```properties
guice.injector-source=com.example.audit.GuiceInjectorSource
```

The tests should work now.