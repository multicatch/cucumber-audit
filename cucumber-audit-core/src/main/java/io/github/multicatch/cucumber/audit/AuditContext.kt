package io.github.multicatch.cucumber.audit

import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.AbstractDriverOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.RemoteWebDriver

interface AuditContext {
    val proxy: BrowserMobProxy
    val driver: RemoteWebDriver
}

@JvmOverloads
fun auditContextOf(
        type: DriverType,
        driverLocation: String? = null
): AuditContext = DefaultAuditContext(type, driverLocation)

class DefaultAuditContext
@JvmOverloads constructor(
        type: DriverType,
        driverLocation: String? = null
) : AuditContext {
    override val proxy: BrowserMobProxy = BrowserMobProxyServer()
    override val driver: RemoteWebDriver = createDriver(proxy, type, driverLocation)

    private fun createDriver(
            proxy: BrowserMobProxy,
            type: DriverType,
            driverLocation: String?
    ): RemoteWebDriver {
        proxy.setTrustAllServers(true)
        proxy.start(0)
        proxy.setHarCaptureTypes(setOf())

        if (driverLocation != null) {
            System.setProperty(type.driverLocationProperty, driverLocation)
        }

        val seleniumProxy = ClientUtil.createSeleniumProxy(proxy)

        return when (type) {
            DriverType.GECKO -> FirefoxDriver(FirefoxOptions().with(seleniumProxy) as FirefoxOptions)
            DriverType.CHROME -> ChromeDriver(ChromeOptions().with(seleniumProxy) as ChromeOptions)
        }
    }

    private fun <T : AbstractDriverOptions<T>> T.with(seleniumProxy: Proxy) = apply {
        setProxy(seleniumProxy)
        setCapability(CapabilityType.PROXY, seleniumProxy)
        setCapability(CapabilityType.ACCEPT_SSL_CERTS, seleniumProxy)
    }
}

enum class DriverType(val driverLocationProperty: String) {
    GECKO("webdriver.gecko.driver"),
    CHROME("webdriver.chrome.driver")
}