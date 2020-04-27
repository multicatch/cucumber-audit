package io.github.multicatch.cucumber.audit

import java.io.File
import java.util.concurrent.TimeUnit

private const val containerName = "cucumber-audit-oauth-app"

class ServerDriver {
    private val workingDirectory = File("cucumber.properties".resourceFile()!!.parent)

    fun start() {
        processOf(
                command = "docker",
                arguments = listOf("run",
                        "-d",
                        "--rm",
                        "-p", "8000:8000",
                        "--name", containerName,
                        "multicatch/django-example-oauth"
                ),
                workingDirectory = workingDirectory,
                output = ProcessBuilder.Redirect.to(File("/dev/null"))
        ).apply {
            waitFor(30, TimeUnit.SECONDS)
        }
    }

    fun stop() {
        processOf(
                command = "docker",
                arguments = listOf("stop", containerName),
                workingDirectory = workingDirectory
        )
    }
}