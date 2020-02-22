package learning.clientTesting

import org.openqa.selenium.By
import org.openqa.selenium.ElementNotVisibleException
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Action
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Reporter
import org.testng.annotations.Test

import java.util.concurrent.TimeUnit

class WebTestActions {

    @Test
    void webTest() {

        System.setProperty("webdriver.chrome.driver", "/users/m52844/Selenium/chromedriver")
        WebDriver oDriver = new ChromeDriver()
        oDriver.get("WWW.google.co.in")
        oDriver.manage().deleteAllCookies()
        oDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS)
        oDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        WebDriverWait webDriverWait = new WebDriverWait(oDriver, 10)

        oDriver.manage().window().maximize()

        WebElement uName = oDriver.findElement(By.id("userName"))

        Actions builder = new Actions(oDriver)
        Action seriesOfAction = builder.moveToElement(uName).keyDown(Keys.SHIFT).sendKeys(uName, "jamsol").keyUp(Keys.SHIFT).doubleClick(uName).contextClick().build()
        seriesOfAction.perform()

        List<WebElement> iFrames = oDriver.findElements(By.tagName("iframe"))
        iFrames.each { frame ->
            oDriver.switchTo().frame(frame)
            try {
                WebElement el1 = oDriver.findElements(By.tagName("img"))
                println(el1.getAttribute("src"))
            } catch (StaleElementReferenceException | ElementNotVisibleException e) {
                Reporter.log(e.getMessage())
            }
            oDriver.switchTo().parentFrame()
        }

//        Alert alert = oDriver.switchTo().alert()
//        alert.getText(); alert.accept(); alert.dismiss();

        JavascriptExecutor js = (JavascriptExecutor) oDriver
        println(oDriver.executeScript("return document.URL").toString())
        oDriver.executeScript("window.scrollBy(0,600)")
        oDriver.executeScript("arguments[0].scrollIntoView(true);", uName)      //Scroll until element is visible
        ExpectedCondition<Boolean> pageLoadCondition = oDriver.executeScript("return document.readyState").toString().matches("interactive | complete")
        webDriverWait.until(pageLoadCondition)

        Thread.sleep(2000)
        oDriver.quit()
    }
}
