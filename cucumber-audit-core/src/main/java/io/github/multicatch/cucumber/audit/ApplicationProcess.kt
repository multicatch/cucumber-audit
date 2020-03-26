package io.github.multicatch.cucumber.audit

import io.cucumber.junit.CucumberAudit
import java.io.File

fun List<String>.asProcess(workingDirectory: File): Process = ProcessBuilder(*toTypedArray())
        .directory(workingDirectory)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()

fun String.resourceFile() = CucumberAudit::class.java.classLoader.getResource(this)?.path?.let {
    File(it)
}