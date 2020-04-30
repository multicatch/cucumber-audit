package io.github.multicatch.cucumber.audit.variables

import io.cucumber.core.gherkin.Feature
import io.github.multicatch.cucumber.audit.plugins.FeatureRedactor
import io.github.multicatch.cucumber.audit.plugins.RedactedStep

open class VariableResolvingRedactor : FeatureRedactor {
    private val resolver by lazy {
        VariableResolverFactory.create()
    }

    override fun redact(feature: Feature) = feature.apply {
        pickles.replaceAll { pickle ->
            pickle.apply {
                steps.replaceAll {
                    RedactedStep(it.id, it.line, it.keyWord, it.type, resolver.replace(it.text), it.argument, it.previousGivenWhenThenKeyWord)
                }
            }
        }
    }

}