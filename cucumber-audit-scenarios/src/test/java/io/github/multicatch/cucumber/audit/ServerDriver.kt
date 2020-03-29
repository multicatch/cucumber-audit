package io.github.multicatch.cucumber.audit

import java.io.File
import java.util.concurrent.TimeUnit

class ServerDriver {
    private val workingDirectory = File("exampleApp/runserver.sh".resourceFile()!!.parent)
    var serverProcess: Process? = null

    fun start() {
        serverProcess = listOf("bash", "runserver.sh").asProcess(workingDirectory)
        serverProcess?.waitFor(30, TimeUnit.SECONDS)
    }

    fun stop() {
        val process = serverProcess ?: throw IllegalStateException("Server has not been started")
        process.outputStream.bufferedWriter().apply {
            newLine()
            flush()
        }
    }
}