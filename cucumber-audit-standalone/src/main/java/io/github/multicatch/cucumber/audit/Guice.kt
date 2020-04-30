package io.github.multicatch.cucumber.audit

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.cucumber.guice.CucumberModules
import io.cucumber.guice.InjectorSource
import io.github.multicatch.cucumber.audit.context.AuditContext

class GuiceInjectorSource : InjectorSource {
    override fun getInjector() = Guice.createInjector(CucumberModules.createScenarioModule(), CucumberModule())
}

class CucumberModule : AbstractModule() {
    override fun configure() {
        binder().bind(AuditContext::class.java).toProvider(AuditContextProvider)
    }
}