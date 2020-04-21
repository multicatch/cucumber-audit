package io.github.multicatch.cucumber.audit.context

import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.AbstractDriverOptions
import org.openqa.selenium.remote.CapabilityType

fun createFirefoxOptions(
        proxy: Proxy,
        headless: Boolean
) = FirefoxOptions().setHeadless(headless).with(proxy) as FirefoxOptions

fun createChromeOptions(
        proxy: Proxy,
        headless: Boolean
) = ChromeOptions().setHeadless(headless).setAcceptInsecureCerts(true).with(proxy) as ChromeOptions

private fun <T : AbstractDriverOptions<T>> T.with(seleniumProxy: Proxy) = apply {
    setProxy(seleniumProxy)
    setCapability(CapabilityType.PROXY, seleniumProxy)
    setCapability(CapabilityType.ACCEPT_SSL_CERTS, seleniumProxy)
}