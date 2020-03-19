package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import javax.inject.Inject

class NavigationStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {

    init {
        When("I go to {string}") { url: String ->
            auditContext.driver.get(url)
        }
    }
}