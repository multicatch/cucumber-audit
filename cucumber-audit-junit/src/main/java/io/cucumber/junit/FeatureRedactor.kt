package io.cucumber.junit

import io.cucumber.core.gherkin.Feature
import io.cucumber.core.plugin.Plugins
import io.github.multicatch.cucumber.audit.plugins.FeatureRedactor
import org.junit.runners.ParentRunner

fun Plugins.runRedactors(children: MutableList<ParentRunner<*>>) {
    val redactors = plugins.filterIsInstance<FeatureRedactor>()
    children.forEach { runner ->
        redactors.fold(runner) { current: ParentRunner<*>, featureRedactor: FeatureRedactor ->
            current.redact { feature ->
                featureRedactor.redact(feature)
            }
        }
    }
}

fun ParentRunner<*>.redact(transformation: (Feature) -> Feature): ParentRunner<*> = apply {
    if (this is FeatureRunner) {
        val declaredField = FeatureRunner::class.java.getDeclaredField("feature")
        declaredField.isAccessible = true
        val feature = declaredField.get(this) as Feature
        declaredField.set(this, transformation(feature))
        declaredField.isAccessible = false
    }
}
