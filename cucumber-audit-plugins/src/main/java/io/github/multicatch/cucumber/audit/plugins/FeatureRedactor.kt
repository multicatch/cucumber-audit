package io.github.multicatch.cucumber.audit.plugins

import io.cucumber.core.gherkin.Argument
import io.cucumber.core.gherkin.Feature
import io.cucumber.core.gherkin.Step
import io.cucumber.core.gherkin.StepType
import io.cucumber.plugin.EventListener
import io.cucumber.plugin.event.EventPublisher

interface FeatureRedactor : EventListener {
    override fun setEventPublisher(publisher: EventPublisher) {
        // unused by cucumber-audit
    }

    fun redact(feature: Feature): Feature
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