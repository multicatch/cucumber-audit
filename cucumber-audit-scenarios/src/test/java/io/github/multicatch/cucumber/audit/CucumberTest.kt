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
import org.junit.BeforeClass
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.TimeUnit

@RunWith(CucumberAudit::class)
@CucumberOptions(plugin = ["pretty"], strict = false)
object CucumberTest {
    lateinit var serverProcess: Process

    @BeforeClass
    @JvmStatic
    fun setup() {
        val workingDirectory = File("exampleApp/runserver.sh".resourceFile()!!.parent)
        serverProcess = listOf("bash", "runserver.sh").asProcess(workingDirectory)
        serverProcess.waitFor(30, TimeUnit.SECONDS)
    }

    @AfterClass
    @JvmStatic
    fun teardown() {
        auditContext.driver.quit()
        serverProcess.destroyForcibly()
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