package io.github.multicatch.cucumber.audit.variables

import java.io.File
import java.util.*

class PropertyVariableResolver(options: Properties) : VariableResolver {
    private val properties = options.getProperty("audit.variable_file")?.let {
        propertiesOf(it)
    } ?: options

    override fun resolve(variableName: String): String {
        return properties.getProperty(variableName)
                ?: error("Cannot resolve \$${variableName} variable - no such entry in property file")
    }

}

fun propertiesOf(fileName: String): Properties = Properties().apply {
    val stream = if (fileName.startsWith("classpath:")) {
        PropertyVariableResolver::class.java.classLoader.getResourceAsStream(fileName.drop("classpath:".length))
    } else {
        File(fileName).inputStream()
    }

    load(stream)
}