# cucumber-audit

Easy automated auditing with preconfigured step definitions

### Sample scenarios

Available [here](cucumber-audit-scenarios/src/main/resources/io/github/multicatch/cucumber/audit)

### How to run sample scenarios

Download a Selenium Gecko driver and run the following in the project directory:

```shell script
mvn clean install -Dwebdriver.gecko.driver=/path/to/geckodriver
```

### Pretty reports

This project uses [`de.monochromata.cucumber:reporting-plugin`](https://gitlab.com/monochromata-de/cucumber-reporting-plugin) to generate pretty reports.

By default, pretty html reports are generated into `cucumber-audit-scenarios/target/cucumber/cucumber-html-reports` directory.

Reports from master branch are deployed and available [here](https://multicatch.github.io/cucumber-audit/master/cucumber-html-reports/overview-features.html).