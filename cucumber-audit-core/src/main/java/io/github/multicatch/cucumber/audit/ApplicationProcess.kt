package io.github.multicatch.cucumber.audit

import io.github.multicatch.cucumber.audit.context.AuditContext
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

fun String.resourceFile() = AuditContext::class.java.classLoader.getResource(this)?.path?.let {
    File(it)
}