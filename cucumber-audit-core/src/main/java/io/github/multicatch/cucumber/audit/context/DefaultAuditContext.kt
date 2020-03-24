package io.github.multicatch.cucumber.audit.context

import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil
import net.lightbody.bmp.filters.RequestFilter
import net.lightbody.bmp.util.HttpMessageContents
import net.lightbody.bmp.util.HttpMessageInfo
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.AbstractDriverOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.RemoteWebDriver

class DefaultAuditContext
@JvmOverloads constructor(
        type: DriverType,
        driverLocation: String? = null
) : AuditContext {
    override val proxy: BrowserMobProxy = BrowserMobProxyServer()
    override val driver: RemoteWebDriver = createDriver(proxy, type, driverLocation)

    override var method: HttpMethod? = null

    private fun createDriver(
            proxy: BrowserMobProxy,
            type: DriverType,
            driverLocation: String?
    ): RemoteWebDriver {
        proxy.setTrustAllServers(true)
        proxy.start(0)
        proxy.setHarCaptureTypes(setOf())
        proxy.addRequestFilter(requestFilter())

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

    private fun requestFilter() = RequestFilter { httpRequest: HttpRequest, _: HttpMessageContents, _: HttpMessageInfo ->
        if (method != null) {
            httpRequest.method = method
        }

        null
    }
}