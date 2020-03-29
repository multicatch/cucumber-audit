package io.github.multicatch.cucumber.audit

import java.io.File
import java.util.concurrent.TimeUnit

class ServerDriver {
    private val workingDirectory = File("exampleApp/runserver.sh".resourceFile()!!.parent)
    private var serverProcess: Process? = null

    fun start() {
        serverProcess = processOf(
                command = "bash",
                arguments = listOf("runserver.sh"),
                workingDirectory = workingDirectory
        ).apply {
            waitFor(30, TimeUnit.SECONDS)
        }
    }

    fun stop() {
        val process = serverProcess ?: throw IllegalStateException("Server has not been started")
        process.outputStream.bufferedWriter().apply {
            newLine()
            flush()
        }
    }
}