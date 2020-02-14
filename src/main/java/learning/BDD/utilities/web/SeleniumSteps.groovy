package learning.BDD.utilities.web

import cucumber.api.Transform
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformTextUsingYAML
import learning.BDD.utilities.transformer.TransformToAssert
import learning.BDD.utilities.transformer.TransformToWebElement
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Assert

import java.lang.annotation.ElementType

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

    @Given("^I click on \"(.*)\" and wait for \"(.*)\" to appear\$")
    void clickAppear(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformToWebElement.class) WebElement waitElement) {
        click(webElement)
        wait_for_visibility(waitElement)
    }

    @Given("^I clear \"(.*)\"\$")
    void clear(@Transform(TransformToWebElement.class) WebElement webElement) {
        webElement.clear()
    }

    @Given("^I enter \"(.*)\" as \"(.*)\"\$")
    void send_keys(@Transform(TransformToWebElement) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        webElement.sendKeys(sValue)
    }

    @Given("^I clear & enter \"(.*)\" as \"(.*)\"\$")
    void clear_and_send_keys(@Transform(TransformToWebElement) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        webElement.clear()
        webElement.sendKeys(sValue)
    }

    @Given("^I (assert|verify) \"(.*)\" is displayed\$")
    void element_displayed(@Transform(TransformToAssert.class) Boolean bAssert, String webElement) {
        if(bAssert) {
            Assert.assertTrue(Context.getInstance().find(webElement))
        } else {
            verifyDisplayed(webElement, true)
        }
    }

    @Given("^I (assert|verify) \"(.*)\" is not displayed\$")
    void element_not_displayed(@Transform(TransformToAssert.class) Boolean bAssert, String webElement) {
        if(bAssert) {
            Assert.assertFalse(Context.getInstance().find(webElement))
        } else {
            verifyDisplayed(webElement, false)
        }
    }

    private void verifyDisplayed(String webElement, Boolean bExpected) {
        if(bExpected) {
            if(!Context.getInstance().find(webElement)) {
                Context.getInstance().getReports().stepFail("Validation failure : WebElement $webElement is not displayed")
            }
        } else {
            if(Context.getInstance().find(webElement)) {
                Context.getInstance().getReports().stepFail("Validation failure : WebElement $webElement is displayed")
            }
        }
    }

    @Given("^I verify \"(.*)\" text is displayed\$")
    void verify_text_with_label(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.TEXT, sValue, true)
    }

    @Given("^I verify \"(.*)\" text is not displayed\$")
    void verify_text_with_label_negative(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.TEXT, sValue, false)
    }

    @Given("^I verify text contains \"(.*)\" is displayed\$")
    void verify_contains_text_with_label(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.CONTAINS_TEXT, sValue, true)
    }

    @Given("^I verify text contains \"(.*)\" is not displayed\$")
    void verify_contains_text_with_label_negative(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.CONTAINS_TEXT, sValue, false)
    }

    @Given("^I verify \"(.*)\" button is displayed\$")
    void verify_button_with_label(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.BUTTON, sValue, true)
    }

    @Given("^I verify button contains \"(.*)\" is displayed\$")
    void verify_contains_button_with_label(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.CONTAINS_BUTTON, sValue, true)
    }

    @Given("^I verify \"(.*)\" link is displayed\$")
    void verify_link_with_label(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.LINK, sValue, true)
    }

    @Given("^I verify link contains \"(.*)\" is displayed\$")
    void verify_contains_link_with_label(String sValue) {
        verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType.CONTAINS_LINK, sValue, true)
    }

    private static void verify_element_with_type_and_text(learning.BDD.utilities.utilEnum.ElementType elementType, String sValue, Boolean bDisplayFlag) {
        String sXpath = ""
        WebElement oWebElement
        switch (elementType) {
            case learning.BDD.utilities.utilEnum.ElementType.TEXT:
                sXpath = (sValue.contains("'") ? "//*[text()=\"" + sValue + "\"]" : "//*[text()='" + sValue + "']")
                break
            case learning.BDD.utilities.utilEnum.ElementType.CONTAINS_TEXT:
                sXpath = (sValue.contains("'") ? "//*[contains(text(),\"" + sValue + "\")]" : "//*[contains(text(),'" + sValue + "')]")
                break
            case learning.BDD.utilities.utilEnum.ElementType.BUTTON:
                sXpath = (sValue.contains("'") ? "//button[text()=\"" + sValue + "\"]" : "//button[text()='" + sValue + "']")
                break
            case learning.BDD.utilities.utilEnum.ElementType.CONTAINS_BUTTON:
                sXpath = (sValue.contains("'") ? "//button[contains(text(),\"" + sValue + "\")]" : "//button[contains(text(),'" + sValue + "')]")
                break
            case learning.BDD.utilities.utilEnum.ElementType.LINK:
                sXpath = (sValue.contains("'") ? "//a[text()=\"" + sValue + "\"]" : "//a[text()='" + sValue + "']")
                break
            case learning.BDD.utilities.utilEnum.ElementType.CONTAINS_LINK:
                sXpath = (sValue.contains("'") ? "//a[contains(text(),\"" + sValue + "\")]" : "//a[contains(text(),'" + sValue + "')]")
                break
            default:
                throw new IllegalArgumentException("$elementType is not a valid argument Type for valid_element_with_type_and_text")
        }

        try {
            oWebElement = (WebElement) Context.getInstance().getWebDriverWait().until(ExpectedConditions.visibilityOf(Context.getInstance().findElement(By.xpath(sXpath))))
            if(bDisplayFlag) {
                Assert.assertTrue(oWebElement.isDisplayed())
            } else {
                Assert.assertFalse(oWebElement.isDisplayed())
            }
        } catch (Exception e) {
            Context.getInstance().getReports().stepFail("Encounter a issue in verify_element_with_type_and_text")
        }
    }

    @Given("^I scroll to see \"(.*)\" webelement\$")
    void scroll_till_webelement_visible(@Transform(TransformToWebElement.class) WebElement webElement) {
        try {
            ((JavascriptExecutor) oDriver).executeScript("arguments[0].scrollIntoView(true);", webElement)
            Context.getInstance().getReports().stepScreenshot()
        } catch (Exception e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }


















}
