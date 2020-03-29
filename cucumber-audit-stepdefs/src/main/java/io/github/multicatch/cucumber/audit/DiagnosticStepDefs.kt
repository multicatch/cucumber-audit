package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL

class DiagnosticStepDefs : En {
    private val logger = LoggerFactory.getLogger(DiagnosticStepDefs::class.java)

    init {
        Given("app under {string} has already started") { address: String ->
            val url = URL(address)
            val interval: Long = 1000
            var tries = 0
            runBlocking {
                while(!url.isAlive()) {
                    tries += 1
                    if (tries % 10 == 0) {
                        logger.info("$address is still down after ${tries*interval/1000}s")
                    }
                    delay(interval)
                }
            }
            logger.debug("$address up after ${tries*interval/1000}s")
        }
    }
}

fun URL.isAlive(): Boolean {
    try {
        with(openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            return responseCode == 200
        }
    } catch (e: Exception) {
        return false
    }
}