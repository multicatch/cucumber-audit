package io.github.multicatch.cucumber.audit.plugins

import io.cucumber.core.gherkin.Argument
import io.cucumber.core.gherkin.Feature
import io.cucumber.core.gherkin.Step
import io.cucumber.core.gherkin.StepType
import io.cucumber.plugin.EventListener
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.Location

interface FeatureRedactor : EventListener {
    @JvmDefault
    override fun setEventPublisher(publisher: EventPublisher) {
        // unused by cucumber-audit
    }

    fun redact(feature: Feature): Feature
}

class RedactedStep(
        private val id: String,
        private val line: Int,
        private val keyword: String?,
        private val stepType: StepType,
        private val text: String?,
        private val argument: Argument?,
        private val previousKeyWord: String?,
        private val location: Location?
) : Step {
    override fun getArgument(): Argument? = argument

    override fun getLine(): Int = line

    override fun getLocation(): Location? = location

    override fun getId(): String = id

    override fun getKeyword(): String? = keyword

    override fun getType(): StepType = stepType

    override fun getPreviousGivenWhenThenKeyword(): String? = previousKeyWord

    override fun getText(): String? = text

}