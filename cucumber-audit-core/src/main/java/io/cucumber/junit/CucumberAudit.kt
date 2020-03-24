package io.cucumber.junit

import io.github.multicatch.cucumber.audit.InitializedTestRedactor
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runners.ParentRunner
import org.junit.runners.model.RunnerScheduler
import org.junit.runners.model.Statement

class CucumberAudit(clazz: Class<*>) : ParentRunner<ParentRunner<*>>(clazz) {
    private val cucumber = Cucumber(clazz)

    init {
        InitializedTestRedactor.redact(children)
    }

    override fun runChild(child: ParentRunner<*>, notifier: RunNotifier) {
        child.run(notifier)
    }

    override fun getChildren(): MutableList<ParentRunner<*>> {
        return cucumber.children
    }

    override fun describeChild(child: ParentRunner<*>): Description {
        return cucumber.describeChild(child)
    }

    override fun childrenInvoker(notifier: RunNotifier?): Statement {
        return cucumber.childrenInvoker(notifier)
    }

    override fun setScheduler(scheduler: RunnerScheduler?) {
        return cucumber.setScheduler(scheduler)
    }
}