package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.github.multicatch.cucumber.audit.context.AuditContext
import jakarta.inject.Inject

class WhitelistStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {
    init {
        Given("only whitelisted traffic is allowed") {
            auditContext.proxy.enableEmptyWhitelist(421)
        }

        Given("traffic matching {string} is allowed") { url: String ->
            auditContext.proxy.addWhitelistPattern(url)
        }
    }
}