package io.github.multicatch.cucumber.audit

import io.github.multicatch.cucumber.audit.context.AuditContext
import io.github.multicatch.cucumber.audit.context.DriverType
import io.github.multicatch.cucumber.audit.context.auditContextOf
import javax.inject.Provider

object AuditContextProvider : Provider<AuditContext> {
    lateinit var auditContext: AuditContext

    override fun get() = auditContext
}

private const val CONTEXT_DRIVER_TYPE = "webdriver.type"
private const val CONTEXT_DRIVER_HEADLESS = "webdriver.headless"

fun Array<out String>.setupAuditContext(options: Map<String, String>) {
    val literalType = CONTEXT_DRIVER_TYPE.from(this, options)
            ?: error("""Please provide webdriver type with "$CONTEXT_DRIVER_TYPE" option.""")

    val type = DriverType.valueOf(literalType.toUpperCase())

    val location = type.driverLocationProperty.from(this, options)
            ?: error("""Please provide webdriver location with "${type.driverLocationProperty}" option.""")

    val literalHeadless = CONTEXT_DRIVER_HEADLESS.from(this, options) ?: "false"
    val headless = literalHeadless.toBoolean()

    AuditContextProvider.auditContext = auditContextOf(
            type = type,
            driverLocation = location,
            headless = headless
    )
}

fun Array<out String>.withoutCucumberAuditOptions() =
        filtered(
                CONTEXT_DRIVER_TYPE,
                *DriverType.values().map { it.driverLocationProperty }.toTypedArray(),
                CONTEXT_DRIVER_HEADLESS
        )

private fun String.from(argv: Array<out String>, options: Map<String, String>): String? {
    val typeFromProperties = options[this]
    val typeFromArguments = with(argv.indexOfFirst { it == "--$this" }) {
        if (this < 0) null else argv.getOrNull(this + 1)
    }
    return typeFromProperties ?: typeFromArguments
}

private fun Array<out String>.filtered(vararg keys: String) = filterIndexed { index, current ->
        !keys.contains(current.asArgumentKey()) && !keys.contains(getOrNull(index -1)?.asArgumentKey())
    }

private fun String.asArgumentKey() = trimStart('-')