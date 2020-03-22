package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.cucumber.java8.Scenario
import io.netty.handler.codec.http.HttpMethod
import javax.inject.Inject

class NavigationStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {

    init {
        Before { _: Scenario ->
            auditContext.method = null
        }

        When("I go to {string}") { url: String ->
            auditContext.driver.get(url)
        }

        When("I make a {string} request to {string}") { method: String, url: String ->
            auditContext.method = HttpMethod.valueOf(method)
            auditContext.driver.get(url)
        }
    }
}