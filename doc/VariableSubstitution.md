# Variable Substitution

## Description

The cucumber-audit library allows to make reusable feature files by using variables in feature files.
The variable substitution plugin is exclusive to the cucumber-audit and is not compatible with standard Cucumber.

## How It Works

This plugin is invoked at the time that the feature files have been read and parsed.
It 'redacts' the features by substituting variables in a `$variable` format with a known value.
If a variable name is not matched, the variable placeholder will be left as is.

It is possible to override variable substitution by escaping the dollar sign.

For example, this plugin will try to resolve the following text:

`$change_me`

But the following text will not be resolved:

`\$leave_me_alone`

## Variable Substitution with cucumber-audit-standalone

To use variable substitution in cucumber-audit-standalone, add the following option:

```
  --plugin io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor
```

Example usage:

```shell script
java -jar cucumber-audit-standalone.jar /home/user/features/sample.feature \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --plugin io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver
```

## Variable Substitution in integrated cucumber-audit

Use the following dependency:

```xml
        <dependency>
            <groupId>io.github.multicatch</groupId>
            <artifactId>cucumber-audit-variables</artifactId>
            <version>${project.version}</version>
        </dependency>
```

And activate the plugin:

```java
@RunWith(CucumberAudit.class)
@CucumberOptions(
    plugin = {
        "pretty",
        "io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor"
    },
    strict = true
)
class CucumberAuditTest {
}
```

## Configuration

This plugin reads all environment variables by default unless you define a variable file. 
This file location can be defined by setting a `AUDIT_VARIABLE_FILE` environment variable or by specifying a
`audit.variable_file` option in `cucumber.properties` file. 
A variable file has to be a file in a `.properties` format.

For example, a following variable file named `variables.properties` can be used:

```properties
url=https://httpbin.org
```

To use it with cucumber-audit-standalone from [Getting Started](GettingStarted.md) example, use the following command:

```shell script
AUDIT_VARIABLE_FILE=variables.properties \
java -jar cucumber-audit-standalone.jar /home/user/features \
  --glue io.github.multicatch.cucumber.audit \
  --plugin pretty \
  --plugin io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor \
  --webdriver.type GECKO \
  --webdriver.gecko.driver /home/user/geckodriver
```

To use this file with an integrated cucumber-audit, specify the following in the `cucumber.properties` file:

```
audit.variable_file=variables.properties
```

You should use a `classpath:` name prefix if the file is available on classpath (in resources folder) 
instead of a standard file system.