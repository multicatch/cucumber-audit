# cucumber-audit documentation

* [Getting Started](GettingStarted.md)
* [Predefined Steps](PredefinedSteps.md)
* [Variable Substitution](VariableSubstitution.md)
* [Sample Threat Model](SampleThreadModel.md)

## FAQ

### What is this and what is it used for?

This tool is used for building custom automated auditing scenarios
using predefined or custom steps. The heart of this tool is Cucumber, 
which provides Gherkin and reports that can be used as a security audit report.

### Where can it be used?

This tool can be used on any machine that supports Java Runtime Environment 8.
It can be integrated into a Java/Kotlin project, but it is also a standalone tool.
It can be used to test any web application that uses HTTP(S).

### Can it be used only for auditing?

Absolutely no! Feel free to do any type of blackbox testing with this tool.
An automated security audit is the primary goal of this tool, but it is not limited to it.
It can be even used for a quick setup of integration tests.

### Do I need to know Java/Kotlin to use this?

You just need English. Scenarios are written in Gherking, which
is just as simple as using English. See the predefined steps for reference.

## Project Modules

### cucumber-audit-context

This module contains the configuration of browser drivers and proxy. 
It is used to configure a context for tests and control the communication between the client (a browser) and the tested application.

### cucumber-audit-junit

This module adapts the cucumber-audit to be used with JUnit. 
It also enables support for Cucumber and cucumber-audit plugins in tests.

### cucumber-audit-plugins

In this module there is an API for custom plugins that will be used during test execution.

### cucumber-audit-stepdefs

This module contains the standard *step definitions* of cucumber-audit. 
Using this module allows to use a predefined steps to build custom scenarios.

### cucumber-audit-variables

This is a plugin that allows to use variables read from configuration file or environment in scenario definition.

### cucumber-audit-scenarios

A set of sample scenarios that can be reused thanks to variable substitution in scenarios.

### cucumber-audit-standalone

This module contains the configuration to build a standalone cucumber-audit jar.

### cucumber-audit-common

This module contains helper functions and models that can be used by any module in this project.