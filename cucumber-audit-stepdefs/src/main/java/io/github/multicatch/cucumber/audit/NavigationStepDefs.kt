package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import io.github.multicatch.cucumber.audit.context.AuditContext
import io.netty.handler.codec.http.HttpMethod
import javax.inject.Inject

class NavigationStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {

    init {
        Before { _: Scenario ->
            auditContext.method = null
            auditContext.headers.clear()
        }

        Given("cookies are cleared") {
            auditContext.driver.manage().deleteAllCookies()
        }

        When("I go to {string}") { url: String ->
            auditContext.driver.get(url)
        }

        When("I use a {string} HTTP method") { method: String ->
            auditContext.method = HttpMethod.valueOf(method)
        }

        When("I add a {string} header with value {string}") { header: String, value: String ->
            auditContext.headers[header] = value
        }

        When("I make a request to {string}") { url: String ->
            auditContext.driver.get(url)
        }
    }
}