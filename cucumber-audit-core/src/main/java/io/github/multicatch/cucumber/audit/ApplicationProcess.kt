package io.github.multicatch.cucumber.audit

import io.cucumber.junit.CucumberAudit
import java.io.File

fun processOf(
        command: String,
        arguments: List<String> = listOf(),
        workingDirectory: File,
        output: ProcessBuilder.Redirect = ProcessBuilder.Redirect.INHERIT,
        error: ProcessBuilder.Redirect = ProcessBuilder.Redirect.INHERIT
): Process = ProcessBuilder(command, *arguments.toTypedArray())
        .directory(workingDirectory)
        .redirectOutput(output)
        .redirectError(error)
        .start()

fun String.resourceFile() = CucumberAudit::class.java.classLoader.getResource(this)?.path?.let {
    File(it)
}