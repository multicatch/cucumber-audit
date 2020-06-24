package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.github.multicatch.cucumber.audit.context.AuditContext
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

        Then("the {string} response header should not contain {string}") { header: String, value: String ->
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
                                .noneMatch { it.contains(value) }
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

        Then("the {string} response header should match {string}") { header: String, expression: String ->
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
                                .anyMatch { it.matches(Regex(expression, RegexOption.DOT_MATCHES_ALL)) }
                    }
        }

        Then("the {string} response header should not match {string}") { header: String, expression: String ->
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
                                .noneMatch { it.matches(Regex(expression, RegexOption.DOT_MATCHES_ALL)) }
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

        Then("the response should contain {string}") { value: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.content.text
                    }
                    .also { contents ->
                        Assertions.assertThat(contents)
                                .anyMatch { it.contains(value) }
                    }
        }

        Then("the response code should be {int}") { code: Int ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.status
                    }
                    .also { statuses ->
                        Assertions.assertThat(statuses)
                                .contains(code)
                    }

        }

        Then("the response code should not be {int}") { code: Int ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.status
                    }
                    .also { statuses ->
                        Assertions.assertThat(statuses)
                                .doesNotContain(code)
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
                                .noneMatch { it.matches(Regex(regex, RegexOption.DOT_MATCHES_ALL)) }
                    }
        }

        Then("the response should match {string}") { regex: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.response.content.text
                    }
                    .also { contents ->
                        Assertions.assertThat(contents)
                                .anyMatch { it.matches(Regex(regex, RegexOption.DOT_MATCHES_ALL)) }
                    }
        }

        Then("the response time should not be longer than {long} ms") { time: Long ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull { entry ->
                        entry.timings.receive
                    }
                    .also { receiveTime ->
                        Assertions.assertThat(receiveTime)
                                .noneMatch { it >= time }
                    }
        }
    }
}