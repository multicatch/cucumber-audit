package io.github.multicatch.cucumber.audit

import io.cucumber.java8.En
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DiagnosticStepDefs : En {
    init {
        Given("app under {string} has already started") { address: String ->
            val url = URL(address)
            runBlocking {
                while(!url.isAlive()) {
                    delay(1000)
                }
            }
        }
    }
}

fun URL.isAlive(): Boolean {
    try {
        with(openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            if (responseCode == 200) {
                return true
            }
        }
    } catch (e: IOException) {
        return false
    }
    return false
}