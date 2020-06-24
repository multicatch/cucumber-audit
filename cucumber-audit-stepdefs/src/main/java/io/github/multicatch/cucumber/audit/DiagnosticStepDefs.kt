package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL

class DiagnosticStepDefs : En {

    init {
        Given("app running on {string} has already started") { address: String ->
            address.waitUntilAlive()
        }

        Given("app running on {string} has already started in less than {int} s") { address: String, maxTime: Int ->
            if(!address.waitUntilAlive(maxTime)) {
                Assertions.fail<Any>("Failed to connect to $address")
            }
        }
    }
}

fun String.waitUntilAlive(timeout: Int? = null): Boolean {
    val url = URL(this)
    val interval: Long = 1000
    var tries = 0
    runBlocking {
        while(!url.isAlive()) {
            tries += 1
            if (tries % 10 == 0) {
                logger.info("$this is still down after ${tries*interval/1000}s")
            }
            if (timeout != null && tries / 10 >= timeout) {
                break
            }
            delay(interval)
        }
    }
    val result = url.isAlive()
    if (result) {
        logger.debug("$this is up after ${tries*interval/1000}s wait")
    } else {
        logger.debug("$this is down after ${tries * interval / 1000}s wait")
    }
    return result
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

private val logger = LoggerFactory.getLogger(DiagnosticStepDefs::class.java)