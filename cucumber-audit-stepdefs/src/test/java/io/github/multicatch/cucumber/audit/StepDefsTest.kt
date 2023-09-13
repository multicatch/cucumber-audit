package io.github.multicatch.cucumber.audit

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.cucumber.guice.CucumberModules.createScenarioModule
import io.cucumber.guice.InjectorSource
import io.cucumber.junit.CucumberAudit
import io.cucumber.junit.CucumberOptions
import io.github.multicatch.cucumber.audit.context.AuditContext
import io.github.multicatch.cucumber.audit.context.DriverType
import io.github.multicatch.cucumber.audit.context.auditContextOf
import org.junit.AfterClass
import org.junit.runner.RunWith

@RunWith(CucumberAudit::class)
@CucumberOptions
object StepDefsTest {
    @AfterClass
    @JvmStatic
    fun teardown() {
        auditContext.driver.quit()
    }
}

val auditContext: AuditContext = auditContextOf(DriverType.GECKO, headless = true)

class GuiceInjectorSource : InjectorSource {
    override fun getInjector() = Guice.createInjector(createScenarioModule(), CucumberModule())
}

class CucumberModule : AbstractModule() {
    override fun configure() {
        binder().bind(AuditContext::class.java).toInstance(auditContext)
    }
}