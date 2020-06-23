package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.github.multicatch.cucumber.audit.context.AuditContext
import org.assertj.core.api.Assertions
import javax.inject.Inject

class DocumentStepDefs @Inject constructor(
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

        Then("there should be element selected by {string} on the page") { selector: String ->
            val elements = auditContext.driver.findElementsByCssSelector(selector)
            Assertions.assertThat(elements).isNotEmpty
        }

        Then("there should be element selected by {string}") { xpath: String ->
            val elements = auditContext.driver.findElementsByXPath(xpath)
            Assertions.assertThat(elements).isNotEmpty
        }

        Then("the document should contain {string}") { text: String ->
            val source = auditContext.driver.pageSource
            Assertions.assertThat(source).contains(text)
        }

        Then("the document should not contain {string}") { text: String ->
            val source = auditContext.driver.pageSource
            Assertions.assertThat(source).doesNotContain(text)
        }

        Then("the document should match {string}") { expression: String ->
            val source = auditContext.driver.pageSource
            Assertions.assertThat(source).containsPattern(expression)
        }

        Then("the document should not match {string}") { expression: String ->
            val source = auditContext.driver.pageSource
            Assertions.assertThat(source).doesNotContainPattern(expression)
        }
    }
}