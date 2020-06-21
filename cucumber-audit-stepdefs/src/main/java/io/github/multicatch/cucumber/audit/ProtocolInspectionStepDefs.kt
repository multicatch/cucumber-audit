package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import io.github.multicatch.cucumber.audit.context.AuditContext
import org.assertj.core.api.Assertions
import java.net.URL
import java.net.URLConnection
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class ProtocolInspectionStepDefs @Inject constructor(
        private val auditContext: AuditContext
) : En {
    private lateinit var urlConnection: URLConnection

    init {
        When("I connect to {string}") { url: String ->
            urlConnection = URL(url).openConnection()
        }

        Then("the connection should be secure") {
            Assertions.assertThat(urlConnection).isInstanceOf(HttpsURLConnection::class.java)
            (urlConnection as HttpsURLConnection).also {
                it.connect()
                Assertions.assertThat(it.serverCertificates).isNotEmpty
            }
        }
    }
}