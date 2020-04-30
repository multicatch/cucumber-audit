package io.github.multicatch.cucumber.audit

import io.cucumber.core.feature.FeatureParser
import io.cucumber.core.options.CommandlineOptionsParser
import io.cucumber.core.options.CucumberProperties
import io.cucumber.core.options.CucumberPropertiesParser
import io.cucumber.core.options.RuntimeOptions
import io.cucumber.core.plugin.PluginFactory
import io.cucumber.core.plugin.Plugins
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.firefox.FirefoxDriverService
import java.util.*
import kotlin.system.exitProcess

fun main(vararg argv: String) {
    val exitStatus = runFeatures(argv, Thread.currentThread().contextClassLoader)
    registerDrivers()
    exitProcess(exitStatus.toInt())
}

fun runFeatures(arguments: Array<out String>, classLoader: ClassLoader): Byte {
    val fromPropertiesFile = CucumberProperties.fromPropertiesFile()
    val fromEnvironment = CucumberProperties.fromEnvironment()
    val fromSystemProperties = CucumberProperties.fromSystemProperties()

    val propertiesFileOptions: RuntimeOptions = CucumberPropertiesParser()
            .parse(fromPropertiesFile)
            .build()

    val environmentOptions: RuntimeOptions = CucumberPropertiesParser()
            .parse(fromEnvironment)
            .build(propertiesFileOptions)

    val systemOptions: RuntimeOptions = CucumberPropertiesParser()
            .parse(fromSystemProperties)
            .build(environmentOptions)

    val runtimeOptions: RuntimeOptions = CommandlineOptionsParser()
            .parse(arguments.withoutCucumberAuditOptions())
            .addDefaultGlueIfAbsent()
            .addDefaultFeaturePathIfAbsent()
            .addDefaultFormatterIfAbsent()
            .addDefaultSummaryPrinterIfAbsent()
            .build(systemOptions)

    arguments.setupAuditContext(fromPropertiesFile + fromEnvironment + fromSystemProperties)

    val featureParser = FeatureParser(UUID::randomUUID)

    val plugins = Plugins(PluginFactory(), runtimeOptions)

    val runtime: io.cucumber.core.runtime.Runtime = io.cucumber.core.runtime.Runtime.builder()
            .withRuntimeOptions(runtimeOptions)
            .withClassLoader { classLoader }
            .withFeatureSupplier(StandaloneFeatureSupplier({ classLoader }, runtimeOptions, featureParser, plugins))
            .build()

    runtime.run()
    AuditContextProvider.auditContext.driver.quit()
    return runtime.exitStatus()
}

fun registerDrivers() {
    ServiceLoader.load(FirefoxDriverService.Builder::class.java)
    ServiceLoader.load(ChromeDriverService.Builder::class.java)
}