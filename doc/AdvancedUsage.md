# Advanced Usage

This framework mainly uses the following other frameworks:

* [Cucumber](https://docs.cucumber.io/) - it provides Gherkin and step definition mechanism.
* [Selenium](https://www.selenium.dev/documentation/en/) - it allows to make requests with a browser and use browser's mechanisms to handle sessions and document rendering.
* [browsermob-proxy](https://github.com/lightbody/browsermob-proxy) - it is used to customize requests and inspect responses.
* [JUnit](https://junit.org/junit4/) - it is used to run tests.

All features of those libraries are available with this framework. 
Cucumber plugins are fully supported and the `AuditContext` gives access
to the Selenium's remote driver and browsermob-proxy instance. The JUnit is used
to run tests, so all features of it are available with cucumber-audit.

## Filtering Requests and Responses

There may be a need to filter requests and responses. 
Some applications need special headers or use a special communication encoding 
and thus there is a need to adapt cucumber-audit to those applications.

To do this, you can use add custom request and response filters with BrowserMobProxy.
Use `getProxy()` method of an `AuditContext` instance to get a browsermob-proxy server handler.

The `BroswerMobProxy` interface defines the following methods:

```
void addResponseFilter(ResponseFilter responseFilter);
void addRequestFilter(RequestFilter requestFilter);
```

Add your filter with this methods and all browser communication will go through them.

Notice: Browsers ignore proxy configuration for `127.0.0.1` and `localhost` by default.
To test your filters with an app running on localhost, use `localhost.`. 
The dot at the end makes the host different than `localhost` to the browser and thanks to it
it is possible to use proxy locally.

## Implementing Custom Feature Redactors

To write a custom feature redactor, use `io.github.multicatch:cucumber-audit-plugins` dependency.

The `FeatureRedactor` interface available here is an extension of the standard Cucumber plugin mechanism.
To create own feature content redactor, implement this interface. 

In this library there is also a `RedactedStep` that implements `Step`. It can be used to create a step with
changed content.

Your custom feature redactor can be activated similarly to a standard Cucumber plugin.

Given your implementation class is `com.example.audit.CustomFeatureRedactor`, 
your test runner class can look like the following:

```java
@RunWith(CucumberAudit.class)
@CucumberOptions(plugin = {
    "com.example.audit.CustomFeatureRedactor"
}, strict = true)
class CucumberAuditTest {
}
```

## Implementing Custom Variable Resolvers

To write a custom variable resolver, use `io.github.multicatch:cucumber-audit-variables` dependency.

There is the `VariableResolver` interface that you need to implement. The default implementation of 
the `String replace(String text)` method works by matching a variable name pattern, extracting the variable name,
and then invoking `String resolve(String variableName)`.

The variable name pattern is the following: `\$[a-zA-Z][a-zA-Z0-9_-]*`.

To use your custom variable resolver, you need to :

1. Set the desired variable resolver in `cucumber.properties` using `audit.variable_resolver` option.
2. Activate the `io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor` plugin.

Sample `cucumber.properties`:

```properties
audit.variable_resolver=com.example.audit.CustomVariableResolver
```

Notice: There are two possibilities of a `VariableResolver` constructor:
* no-args constructor,
* constructor with 1 argument of type `java.util.Properties`.

Any other constructor will make the test crash with the following message: `Cannot create an instance of $resolverClass`.