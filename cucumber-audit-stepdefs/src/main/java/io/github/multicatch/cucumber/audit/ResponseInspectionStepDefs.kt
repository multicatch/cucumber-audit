package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import net.lightbody.bmp.proxy.CaptureType
import org.assertj.core.api.Assertions
import javax.inject.Inject

class ResponseInspectionStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {

    init {
        Given("the response headers are under inspection") {
            auditContext.proxy.enableHarCaptureTypes(CaptureType.RESPONSE_HEADERS)
            auditContext.proxy.newHar()
        }

        Given("the response content is under inspection") {
            auditContext.proxy.enableHarCaptureTypes(CaptureType.RESPONSE_CONTENT)
            auditContext.proxy.newHar()
        }

        Then("the {string} response header should contain {string}") { header: String, value: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.headers
                                .map { it.name to it.value }
                                .toMap()[header]
                    }
                    .also { headers ->
                        Assertions.assertThat(headers)
                                .anyMatch { it.contains(value) }
                    }
        }

        Then("the {string} response header should not contain numbers") { header: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.headers
                                .map { it.name to it.value }
                                .toMap()[header]
                    }
                    .also { headers ->
                        Assertions.assertThat(headers)
                                .noneMatch { it.contains(Regex("[0-9]+")) }
                    }
        }

        Then("the response should not contain {string}") { value: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.content.text
                    }
                    .also { contents ->
                        Assertions.assertThat(contents)
                                .noneMatch { it.contains(value) }
                    }
        }


        Then("the response should not match {string}") { regex: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.content.text
                    }
                    .also { contents ->
                        Assertions.assertThat(contents)
                                .noneMatch { it.contains(Regex(regex)) }
                    }
        }
    }
}