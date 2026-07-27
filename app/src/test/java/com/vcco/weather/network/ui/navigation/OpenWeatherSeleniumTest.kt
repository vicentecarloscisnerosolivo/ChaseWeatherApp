package com.vcco.weather.network.ui.navigation

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.URL

class OpenWeatherSeleniumTest {
    private lateinit var driver: AndroidDriver

    @Before
    fun setUp() {
        val options = UiAutomator2Options()
        options.setPlatformName("Android")
        options.setAutomationName("UiAutomator2")
        options.setDeviceName("39161FDJH0060V")
        options.setAppPackage("com.vcco.weather")
        options.setAppActivity(".MainActivity")

        options.setApp("D:\\Documentos\\Github\\ChaseWeatherApp\\app\\build\\outputs\\apk\\debug\\app-debug.apk")

        driver = AndroidDriver(URL("http://192.168.56.1:4723/"), options)
    }

    @Test
    fun testClick() {
        val searchBox =
            driver.findElement(
                AppiumBy.androidViewTag("Type City or Zip Code"),
            )

        val searchButton =
            driver.findElement(
                AppiumBy.androidViewTag("Search"),
            )

        searchBox.clear()
        searchBox.sendKeys("Irving, Texas")

        searchButton.click()
    }

    @After
    fun tearDown() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }
}
