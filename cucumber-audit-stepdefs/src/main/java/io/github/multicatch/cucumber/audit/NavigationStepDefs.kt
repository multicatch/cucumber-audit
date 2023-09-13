package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import io.github.multicatch.cucumber.audit.context.AuditContext
import io.netty.handler.codec.http.HttpMethod
import jakarta.inject.Inject

class NavigationStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {

    init {
        Before { _: Scenario ->
            auditContext.requestSettings.method = null
            auditContext.requestSettings.request = null
            auditContext.requestSettings.headers.clear()
        }

        Given("cookies are cleared") {
            auditContext.driver.manage().deleteAllCookies()
        }

        Given("I am on {string}") { url: String ->
            auditContext.driver.get(url)
        }

        When("I go to {string}") { url: String ->
            auditContext.driver.get(url)
        }

        When("I use a {string} HTTP method") { method: String ->
            auditContext.requestSettings.method = HttpMethod.valueOf(method)
        }

        When("I add a {string} header with value {string}") { header: String, value: String ->
            auditContext.requestSettings.headers[header] = value
        }

        When("the request body is {string}") { body: String ->
            auditContext.requestSettings.request = body
        }

        When("I make a request to {string}") { url: String ->
            auditContext.driver.get(url)
            auditContext.requestSettings.method = null
            auditContext.requestSettings.headers.clear()
            auditContext.requestSettings.request = null
        }
    }
}