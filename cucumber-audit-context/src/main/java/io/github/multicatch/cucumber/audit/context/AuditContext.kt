package io.github.multicatch.cucumber.audit.context

import io.netty.handler.codec.http.HttpMethod
import net.lightbody.bmp.BrowserMobProxy
import org.openqa.selenium.remote.RemoteWebDriver

interface AuditContext {
    val proxy: BrowserMobProxy
    val driver: RemoteWebDriver

    val requestSettings: RequestSettings
}

interface RequestSettings {
    var method: HttpMethod?
    var request: String?
    var headers: MutableMap<String, String>
}

@JvmOverloads
fun auditContextOf(
        type: DriverType,
        driverLocation: String? = null,
        headless: Boolean = false
): AuditContext = DefaultAuditContext(type, driverLocation, headless)

enum class DriverType(val driverLocationProperty: String) {
    GECKO("webdriver.gecko.driver"),
    CHROME("webdriver.chrome.driver")
}