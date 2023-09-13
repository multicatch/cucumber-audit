package io.github.multicatch.cucumber.audit.plugins

import io.cucumber.core.gherkin.StepType
import io.cucumber.plugin.event.Location
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RedactedStepTest {

    @Test
    fun `Redacted step should be compatible with Cucumber's Step`() {
        val redactedStep = RedactedStep(
                id = "test",
                line = 1,
                keyword = "Given",
                stepType = StepType.GIVEN,
                text = "it is a test",
                argument = null,
                previousKeyWord = null,
                location = Location(1, 1)
        )

        assertEquals(redactedStep.id, "test")
        assertEquals(redactedStep.line, 1)
        assertEquals(redactedStep.keyword, "Given")
        assertEquals(redactedStep.type, StepType.GIVEN)
        assertEquals(redactedStep.text, "it is a test")
        assertEquals(redactedStep.argument, null)
        assertEquals(redactedStep.previousGivenWhenThenKeyword, null)
        assertEquals(redactedStep.location, Location(1, 1))
    }
}