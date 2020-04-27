package io.github.multicatch.cucumber.audit

import java.io.File
import java.net.URL


fun String.resourceFile(): File? {
    return File((ResourceLocator.resource(this) ?: return null).path)
}

object ResourceLocator {
    private val classLoader: ClassLoader = ResourceLocator::class.java.classLoader

    fun resource(name: String): URL? = classLoader.getResource(name)
}