package io.github.multicatch.cucumber.audit.variables

import io.github.multicatch.cucumber.audit.resourceFile
import java.io.File
import java.util.*

const val PROPERTY_VARIABLE_FILE = "audit.variable_file"
const val PROPERTY_ENVIRONMENT_VARIABLE = "AUDIT_VARIABLE_FILE"

class PropertyVariableResolver(options: Properties) : VariableResolver {
    private val properties = options.getOneOf(PROPERTY_VARIABLE_FILE, PROPERTY_ENVIRONMENT_VARIABLE)
            ?.let {
                propertiesOf(it)
            } ?: options

    override fun resolve(variableName: String): String {
        return properties.getProperty(variableName)
                ?: "\$$variableName"
    }

}

fun Properties.getOneOf(vararg properties: String): String? {
    for (property in properties) {
        val result = getProperty(property)
        if (result != null) {
            return result
        }
    }

    return null
}

fun propertiesOf(fileName: String): Properties = Properties().apply {
    val stream = if (fileName.startsWith("classpath:")) {
        fileName.drop("classpath:".length).resourceFile()!!.inputStream()
    } else {
        File(fileName).inputStream()
    }

    load(stream)
}