package io.github.multicatch.cucumber.audit

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.cucumber.guice.CucumberModules.createScenarioModule
import io.cucumber.guice.InjectorSource
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.AfterClass
import org.junit.runner.RunWith


@RunWith(Cucumber::class)
@CucumberOptions(plugin = ["pretty"], strict = false)
object CucumberTest {
    @AfterClass
    @JvmStatic
    fun teardown() {
        auditContext.driver.quit()
    }
}

val auditContext: AuditContext = auditContextOf(DriverType.GECKO)

class GuiceInjectorSource : InjectorSource {
    override fun getInjector() = Guice.createInjector(createScenarioModule(), CucumberModule())
}

class CucumberModule : AbstractModule() {
    override fun configure() {
        binder().bind(AuditContext::class.java).toInstance(auditContext)
    }
}