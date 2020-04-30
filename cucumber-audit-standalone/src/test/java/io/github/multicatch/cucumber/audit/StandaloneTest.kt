package io.github.multicatch.cucumber.audit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class StandaloneTest {
    @Test
    fun `Should execute standalone tests`() {
        val arguments = arrayOf(
            "classpath:features",
                "--glue", "io.github.multicatch.cucumber.audit",
                "--plugin", "pretty",
                "--plugin", "io.github.multicatch.cucumber.audit.variables.VariableResolvingRedactor",
                "--webdriver.type", "GECKO",
                "--webdriver.headless", "true"
        )

        Assertions.assertThatCode {
            runFeatures(arguments, StandaloneTest::class.java.classLoader)
        }.doesNotThrowAnyException()
    }

    @Test
    fun `Should fail without webdriver`() {
        val arguments = arrayOf(
                "classpath:features",
                "--glue", "io.github.multicatch.cucumber.audit",
                "--plugin", "pretty"
        )

        Assertions.assertThatThrownBy {
            runFeatures(arguments, StandaloneTest::class.java.classLoader)
        }.isInstanceOf(IllegalStateException::class.java)
    }
}