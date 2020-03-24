package io.github.multicatch.cucumber.audit.variables

import io.cucumber.core.options.Constants
import io.cucumber.core.options.CucumberProperties
import java.util.*

interface VariableResolver {
    fun replace(text: String?): String? =
            if (text == null) {
                null
            } else {
                Regex("\\$[a-zA-Z][a-zA-Z0-9_-]*").replace(text) {
                    resolve(it.value.drop(1))
                }
            }

    fun resolve(variableName: String): String
}

object VariableResolverFactory {

    fun create(): VariableResolver {
        val options = Properties().apply {
            load(CucumberProperties::class.java.getResourceAsStream("/" + Constants.CUCUMBER_PROPERTIES_FILE_NAME))
        }

        val resolverClass = options.getProperty("audit.variable_resolver", PropertyVariableResolver::class.java.canonicalName)
        val loadedClass = VariableResolverFactory::class.java.classLoader.loadClass(resolverClass)

        return loadedClass.constructors
                .find { constructor ->
                    constructor.parameterCount == 0
                }
                ?.newInstance() as VariableResolver?
                ?: loadedClass.constructors
                        .find { constructor ->
                            constructor.parameterTypes.let { it.size == 1 && Properties::class.java == it.first() }
                        }
                        ?.newInstance(options) as VariableResolver?
                ?: error("Cannot create an instance of $resolverClass")
    }
}