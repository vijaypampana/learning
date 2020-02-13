package learning.BDD.utilities.web

import cucumber.api.Transform
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformToWebElement
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait

class SeleniumSteps {

    private WebDriver oDriver
    private WebDriverWait oWebDriverWait

    SeleniumSteps() {
        if(oDriver == null) {
            oDriver = Context.getInstance().getWebDriver()
            oWebDriverWait = Context.getInstance().getWebDriverWait()
        }
    }


    @Given("^I click on \"(.*)\"\$")
    void click(@Transform(TransformToWebElement.class) WebElement webElement) {
        ((JavascriptExecutor) oDriver).executeScript("argument[0].click();", webElement)
    }

    @Given("^I click on \"(.*)\" and wait for \"(.*)\" to disappear\$")
    void clickDisappear(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformToWebElement.class) WebElement waitElement) {
        click(webElement)
        wait_for_invisibility(waitElement)
    }

}
