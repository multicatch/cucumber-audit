package io.github.multicatch.cucumber.audit.context

import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil
import net.lightbody.bmp.filters.RequestFilter
import net.lightbody.bmp.util.HttpMessageContents
import net.lightbody.bmp.util.HttpMessageInfo
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver

// WARNING: Class excluded from coverage, please refrain from major changes
class DefaultAuditContext
@JvmOverloads constructor(
        type: DriverType,
        driverLocation: String? = null,
        headless: Boolean = false
) : AuditContext {
    override val proxy: BrowserMobProxy = BrowserMobProxyServer()
    override val driver: RemoteWebDriver = createDriver(proxy, type, driverLocation, headless)

    override var method: HttpMethod? = null
    override var headers: MutableMap<String, String> = mutableMapOf()

    private val requestFilter = RequestFilter { httpRequest: HttpRequest, _: HttpMessageContents, _: HttpMessageInfo ->
        if (method != null) {
            httpRequest.method = method
        }

        headers.forEach { (name, value) ->
            httpRequest.headers()[name] = value
        }

        null
    }

    private fun createDriver(
            proxy: BrowserMobProxy,
            type: DriverType,
            driverLocation: String?,
            headless: Boolean
    ): RemoteWebDriver {
        proxy.setTrustAllServers(true)
        proxy.start(0)
        proxy.setHarCaptureTypes(setOf())
        proxy.addRequestFilter(requestFilter)

        if (driverLocation != null) {
            System.setProperty(type.driverLocationProperty, driverLocation)
        }

        val seleniumProxy = ClientUtil.createSeleniumProxy(proxy)

        return when (type) {
            DriverType.GECKO -> FirefoxDriver(createFirefoxOptions(seleniumProxy, headless))
            DriverType.CHROME -> ChromeDriver(createChromeOptions(seleniumProxy, headless))
        }
    }
}