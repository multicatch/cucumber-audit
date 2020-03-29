package io.github.multicatch.cucumber.audit.context

import io.netty.handler.codec.http.HttpMethod
import net.lightbody.bmp.BrowserMobProxy
import org.openqa.selenium.remote.RemoteWebDriver

interface AuditContext {
    val proxy: BrowserMobProxy
    val driver: RemoteWebDriver

    var method: HttpMethod?
    var headers: MutableMap<String, String>
}

@JvmOverloads
fun auditContextOf(
        type: DriverType,
        driverLocation: String? = null
): AuditContext = DefaultAuditContext(type, driverLocation)

enum class DriverType(val driverLocationProperty: String) {
    GECKO("webdriver.gecko.driver"),
    CHROME("webdriver.chrome.driver")
}