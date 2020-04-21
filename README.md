# cucumber-audit

[![Build Status](https://travis-ci.org/multicatch/cucumber-audit.svg)](https://travis-ci.org/multicatch/cucumber-audit) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=multicatch_cucumber-audit&metric=alert_status)](https://sonarcloud.io/dashboard?id=multicatch_cucumber-audit)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=multicatch_cucumber-audit&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=multicatch_cucumber-audit)

Easy automated auditing with preconfigured step definitions

### Sample scenarios

Available [here](cucumber-audit-scenarios/src/main/resources/io/github/multicatch/cucumber/audit)

### How to run sample scenarios

Download a Selenium Gecko driver and run the following in the project directory:

```shell script
mvn test -DfailIfNoTests=false -Dtest=CucumberTest -Dwebdriver.gecko.driver=/path/to/geckodriver
```

### Pretty reports

This project uses [`de.monochromata.cucumber:reporting-plugin`](https://gitlab.com/monochromata-de/cucumber-reporting-plugin) to generate pretty reports.

By default, pretty html reports are generated into `cucumber-audit-scenarios/target/cucumber/cucumber-html-reports` directory.

Reports from master branch are deployed and available [here](https://multicatch.github.io/cucumber-audit/master/cucumber-html-reports/overview-features.html).