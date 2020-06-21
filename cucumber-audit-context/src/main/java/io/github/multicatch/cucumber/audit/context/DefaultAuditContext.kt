package io.github.multicatch.cucumber.audit.context

import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponse
import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil
import net.lightbody.bmp.filters.RequestFilter
import net.lightbody.bmp.util.HttpMessageContents
import net.lightbody.bmp.util.HttpMessageInfo
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver

class DefaultAuditContext
@JvmOverloads constructor(
        type: DriverType,
        driverLocation: String? = null,
        headless: Boolean = false
) : AuditContext {
    override val requestSettings: RequestSettings = DefaultRequestSettings()
    override val proxy: BrowserMobProxy = BrowserMobProxyServer()
    override val driver: RemoteWebDriver = createDriver(type, driverLocation, headless)

    private fun createDriver(
            type: DriverType,
            driverLocation: String?,
            headless: Boolean
    ): RemoteWebDriver {
        proxy.setTrustAllServers(true)
        proxy.start(0)
        proxy.setHarCaptureTypes(setOf())
        proxy.addRequestFilter(CucumberAuditRequestFilter(requestSettings))

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

class DefaultRequestSettings : RequestSettings {
    override var method: HttpMethod? = null
    override var request: String? = null
    override var headers: MutableMap<String, String> = mutableMapOf()
}

class CucumberAuditRequestFilter(
        private val requestSettings: RequestSettings
) : RequestFilter {
    override fun filterRequest(httpRequest: HttpRequest, content: HttpMessageContents, info: HttpMessageInfo): HttpResponse? {
        val method = requestSettings.method
        if (method != null) {
            httpRequest.method = method
        }

        requestSettings.headers.forEach { (name, value) ->
            httpRequest.headers()[name] = value
        }

        requestSettings.request?.let {
            val contents = it.toByteArray()
            content.binaryContents = contents
            httpRequest.headers().remove("Content-Length")
            httpRequest.headers().remove("content-length")
            httpRequest.headers().add("Content-Length", contents.size)
        }

        return null
    }
}