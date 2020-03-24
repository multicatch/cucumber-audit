package io.cucumber.junit

import io.cucumber.core.gherkin.Argument
import io.cucumber.core.gherkin.Feature
import io.cucumber.core.gherkin.Step
import io.cucumber.core.gherkin.StepType
import org.junit.runners.ParentRunner

fun ParentRunner<*>.redact(transformation: (Feature) -> Feature): ParentRunner<*> {
    if (this is FeatureRunner) {
        val declaredField = FeatureRunner::class.java.getDeclaredField("feature")
        declaredField.isAccessible = true
        val feature = declaredField.get(this) as Feature
        declaredField.set(this, transformation(feature))
        declaredField.isAccessible = false
    }
    return this
}

class RedactedStep(
        private val id: String,
        private val line: Int,
        private val keyWord: String?,
        private val stepType: StepType,
        private val text: String?,
        private val argument: Argument?,
        private val previousKeyWord: String?
) : Step {
    override fun getPreviousGivenWhenThenKeyWord(): String? = previousKeyWord

    override fun getArgument(): Argument? = argument

    override fun getLine(): Int = line

    override fun getId(): String = id

    override fun getKeyWord(): String? = keyWord

    override fun getType(): StepType = stepType

    override fun getText(): String? = text

}