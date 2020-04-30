package io.github.multicatch.cucumber.audit

import io.cucumber.core.feature.FeatureParser
import io.cucumber.core.feature.Options
import io.cucumber.core.gherkin.Feature
import io.cucumber.core.plugin.Plugins
import io.cucumber.core.runtime.FeaturePathFeatureSupplier
import io.cucumber.core.runtime.FeatureSupplier
import io.github.multicatch.cucumber.audit.plugins.FeatureRedactor
import java.util.function.Supplier

class StandaloneFeatureSupplier(
        classLoader: () -> ClassLoader,
        featureOptions: Options,
        featureParser: FeatureParser,
        private val plugins: Plugins
) : FeatureSupplier {

    private val featurePathFeatureSupplier = FeaturePathFeatureSupplier(Supplier { classLoader() }, featureOptions, featureParser)

    private val redactors by lazy {
        plugins.plugins.filterIsInstance<FeatureRedactor>()
    }

    override fun get() = featurePathFeatureSupplier.get()
            .map { feature: Feature ->
                redactors.fold(feature) { currentFeature: Feature, featureRedactor: FeatureRedactor ->
                    featureRedactor.redact(currentFeature)
                }
            }
            .toMutableList()
}