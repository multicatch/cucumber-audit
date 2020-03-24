package io.github.multicatch.cucumber.audit

import io.cucumber.junit.RedactedStep
import io.cucumber.junit.redact
import io.github.multicatch.cucumber.audit.variables.VariableResolverFactory
import org.junit.runners.ParentRunner

object InitializedTestRedactor {
    val chain: MutableList<TestRedactor> = mutableListOf(VariableResolverRedactor)

    fun redact(children: MutableList<ParentRunner<*>>) {
        chain.forEach {
            it.invoke(children)
        }
    }
}

interface TestRedactor: (MutableList<ParentRunner<*>>) -> Unit

object VariableResolverRedactor: TestRedactor {
    override fun invoke(children: MutableList<ParentRunner<*>>) {
        val resolver = VariableResolverFactory.create()

        children.replaceAll { runner ->
            runner.redact {
                it.apply {
                    pickles.replaceAll { pickle ->
                        pickle.apply {
                            steps.replaceAll {
                                RedactedStep(it.id, it.line, it.keyWord, it.type, resolver.replace(it.text), it.argument, it.previousGivenWhenThenKeyWord)
                            }
                        }
                    }
                }
            }
        }
    }

}