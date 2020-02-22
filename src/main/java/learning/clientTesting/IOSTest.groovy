package learning.clientTesting

import io.appium.java_client.AppiumDriver
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.testng.annotations.Test

class IOSTest {

    @Test
    void IOSSetup() {
        DesiredCapabilities caps = new DesiredCapabilities()
        caps.setCapability("platformName", "ios")
        caps.setCapability("platformVersion", "12.0")
        caps.setCapability("deviceName", "iPhone X")
        caps.setCapability("udid", "")
        caps.setCapability(CapabilityType.BROWSER_NAME, "Safari")
        caps.setCapability("newCommandTimeout", "300")
        caps.setCapability("clearSystemFiles", true)
//        caps.setCapability("app", "/Users/m52844/IdeaProjects/apps/ios/myCigna.ipa")
        URL url = new URL("http://127.0.0.1:4723/wd/hub")
        AppiumDriver appiumDriver = new AppiumDriver(url, caps)

//        appiumDriver.installApp("/Users/m52844/IdeaProjects/apps/ios/myCigna.ipa")
        appiumDriver.get("www.google.com")
    }
}
