package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import javax.inject.Inject

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