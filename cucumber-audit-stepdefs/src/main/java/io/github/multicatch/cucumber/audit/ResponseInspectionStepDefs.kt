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

        Then("a {string} response header should contain {string}") { header: String, value: String ->
            auditContext.proxy.har
                    .log
                    .entries
                    .mapNotNull {
                        it.response.headers
                                .map { it.name to it.value }
                                .toMap()[header]
                    }
                    .also { headers ->
                        Assertions.assertThat(headers)
                                .anyMatch { it.contains(value) }
                    }
        }
    }
}