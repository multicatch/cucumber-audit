package io.github.multicatch.cucumber.audit.variables

import io.github.multicatch.cucumber.audit.resourceFile
import java.io.File
import java.util.*

class PropertyVariableResolver(options: Properties) : VariableResolver {
    private val properties = options.getProperty("audit.variable_file")?.let {
        propertiesOf(it)
    } ?: options

    override fun resolve(variableName: String): String {
        return properties.getProperty(variableName)
                ?: "\$$variableName"
    }

}

fun propertiesOf(fileName: String): Properties = Properties().apply {
    val stream = if (fileName.startsWith("classpath:")) {
        fileName.drop("classpath:".length).resourceFile()!!.inputStream()
    } else {
        File(fileName).inputStream()
    }

    load(stream)
}