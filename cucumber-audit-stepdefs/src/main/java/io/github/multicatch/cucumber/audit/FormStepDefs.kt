package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.github.multicatch.cucumber.audit.context.AuditContext
import javax.inject.Inject

class FormStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {
    init {
        When("I enter {string} into a field selected by {string}") { text: String, selector: String ->
            auditContext.driver.findElementsByCssSelector(selector).forEach {
                it.sendKeys(text)
            }
        }

        When("I click on {string}") { selector: String ->
            auditContext.driver.findElementsByCssSelector(selector).forEach {
                it.click()
            }
        }
    }
}