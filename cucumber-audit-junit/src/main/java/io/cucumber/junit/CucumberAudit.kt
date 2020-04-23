package io.cucumber.junit

import io.cucumber.core.plugin.Plugins
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runners.ParentRunner
import org.junit.runners.model.RunnerScheduler
import org.junit.runners.model.Statement

class CucumberAudit(clazz: Class<*>) : ParentRunner<ParentRunner<*>>(clazz) {
    private val cucumber = Cucumber(clazz)
    private val plugins: Plugins by lazy {
        val declaredField = Cucumber::class.java.getDeclaredField("plugins")
        declaredField.isAccessible = true
        val plugins = declaredField.get(cucumber) as Plugins
        declaredField.isAccessible = false

        plugins
    }

    init {
        plugins.runRedactors(children)
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