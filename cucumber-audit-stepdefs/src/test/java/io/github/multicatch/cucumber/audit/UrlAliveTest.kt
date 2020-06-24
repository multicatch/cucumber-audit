package io.github.multicatch.cucumber.audit

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions
import org.junit.Test
import java.net.URL

class UrlAliveTest {

    private val server = WireMockServer(wireMockConfig().port(8089))
    private val address = "http://localhost:8089/"

    @Test
    fun `should wait for application startup`() {
        GlobalScope.launch {
            delay(2000)
            server.start()
            server.stubFor(get(urlEqualTo("/"))
                    .willReturn(aResponse()
                            .withStatus(200)))
        }
        address.waitUntilAlive()
        Assertions.assertThat(URL(address).isAlive()).isTrue()
        server.stop()
    }

    @Test
    fun `should fail when application does not start with timeout`() {
        Assertions.assertThatThrownBy {
            address.waitUntilAlive(timeout = 1000)
        }.isInstanceOf(AssertionError::class.java)
                .hasMessage("Failed to connect to $address")
    }

}