package learning.BDD.utilities.web

import cucumber.api.Transform
import cucumber.api.java.en.Given
import cucumber.api.java8.Ar
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformTextUsingYAML
import learning.BDD.utilities.transformer.TransformToAssert
import learning.BDD.utilities.transformer.TransformToCssColor
import learning.BDD.utilities.transformer.TransformToWebElement
import learning.BDD.utilities.transformer.TransformToWebElements
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Assert

import java.lang.annotation.ElementType
import java.time.Duration
import java.util.concurrent.TimeoutException

class SeleniumSteps {

    private WebDriver oDriver
    private WebDriverWait oWebDriverWait
    private String sParentWindow = ""

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

    @Given("^I verify \"(.*)\" contains link as \"(.*)\"\$")
    void verify_link_partial_content(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        Assert.assertTrue(webElement.findElement(By.xpath(".//a")).getText().contains(sValue))
    }

    @Given("^I verify \"(.*)\" contains button as \"(.*)\"\$")
    void verify_button_partial_content(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        Assert.assertTrue(webElement.findElement(By.xpath(".//button")).getText().contains(sValue))
    }

    @Given("^I verify \"(.*)\" contains text as \"(.*)\"\$")
    void verify_object_partial_content(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        Assert.assertTrue(webElement.getText().contains(sValue))
    }

    @Given("^I select \"(.*)\" as \"(.*)\"\$")
    void select(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        new Select(webElement).selectByVisibleText(sValue)
    }

    //Wait methods
    //TBD
    void waitForLoad(WebDriver oDriver) {
        try {
            oWebDriverWait.until(((JavascriptExecutor) driver).executeScript("return.document.readyState").toString().matches("interactive | complete"))
        } catch (Exception e) {

        }
    }

    @Given("^I wait for \"(.*)\" to be visible\$")
    void wait_for_visibility(@Transform(TransformToWebElement.class) WebElement webElement) {
        oWebDriverWait.until(ExpectedConditions.visibilityOf(webElement))
    }

    //Method with Fluent Wait example
    @Given("^I wait for \"(.*)\" to be invisible\$")
    void wait_for_invisibility(@Transform(TransformToWebElement.class) WebElement webElement) {
        try {
            FluentWait wait = new FluentWait(Context.getInstance().getWebDriver()).withTimeout(Duration.ofSeconds(Context.getInstance().getConfig().getWebDriverTimeOut())).
                    ignoring(Exception.class).ignoring(TimeoutException.class).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)

            wait.until(ExpectedConditions.invisibilityOf(webElement))

        } catch (TimeoutException e) {

        }
    }

    @Given("^I wait for \"(.*)\" to be clickable\$")
    void wait_for_clickable(@Transform(TransformToWebElement.class) WebElement webElement) {
        oWebDriverWait.until(ExpectedConditions.elementToBeClickable(webElement))
    }

    @Given("^I wait for \"(.*)\" to have text as \"(.*)\"\$")
    void wait_for_element_with_text(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sText) {
        oWebDriverWait.until(ExpectedConditions.textToBePresentInElement(webElement, sText))
    }

    @Given("^I wait for \"(.*)\" to have value as \"(.*)\"\$")
    void wait_for_element_with_value(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        oWebDriverWait.until(ExpectedConditions.textToBePresentInElementValue(webElement, sValue))
    }

    @Given("^I switch to child window\$")
    void switchWindow() {
        sParentWindow = Context.getInstance().getWebDriver().getWindowHandle()
        List<String> multipleWindows = new ArrayList<>()
        waitForLoad(oDriver)
        if(multipleWindows.size() > 1) {
            multipleWindows.each { window ->
                if(!window.equalsIgnoreCase(sParentWindow)) {
                    Context.getInstance().getWebDriver().switchTo().window(window)
                    waitForLoad(oDriver)
                    return
                }
            }
        } else {
            Context.getInstance().getReports().stepFail("Failed to switch into child window - Child window not available")
        }
    }

    @Given("^I switch back to parent window\$")
    void switch_parent_window() {
        Context.getInstance().getWebDriver().close()
        Context.getInstance().getWebDriver().switchTo().window(sParentWindow)
    }

    @Given("^I switch to iFrame \"(.*)\"\$")
    void iSwitchToIframe(@Transform(TransformToWebElement.class) WebElement webElement) {
        oDriver.switchTo().frame(webElement)
    }

    @Given("^I switch to parent frame\$")
    void iSwitchToParentFrame() {
        oDriver.switchTo().parentFrame()
    }

    @Given("^I verify \"(.*)\"  is enabled\$")
    void verify_element_enabled(String sObject) {
        verifyEnabled(sObject, true)
    }

    @Given("^I verify \"(.*)\" is disabled\$")
    void verify_element_disabled(String sObject) {
        verifyEnabled(sObject, false)
    }


    void verifyEnabled(@Transform(TransformToWebElement.class) WebElement webElement, Boolean sExpectedValue) {
        try {
            if (sExpectedValue) {
                Assert.assertTrue(webElement.isDisplayed())
            } else {
                Assert.assertFalse(webElement.isDisplayed())
            }
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail("Assertion Error on the webElement : $webElement")
        }
    }

    @Given("^I verify \"(.*)\" is selected\$")
    void verify_selected(String sObject) {
        verifySelected(sObject, true)
    }

    @Given("^I verify \"(.*)\" is not selected\$")
    void verify_not_selected(String sObject) {
        verifySelected(sObject, false)
    }

    @Given("^I (verify | assert) \"(.*)\" drop down is selected as \"(.*)\"\$")
    void dropDownDefault(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToWebElement.class) WebElement webElement, String sValue) {
        try {
            Assert.assertEquals(new Select(webElement).getFirstSelectedOption().getText(), sValue)
        } catch (AssertionError e) {
            if(bAssert) {
                Context.getInstance().getReports().stepFail(e.getMessage())
            }
        }
    }

    void verifySelected(@Transform(TransformToWebElement.class) WebElement webElement, Boolean bExpected) {
        try {
            if (bExpected) {
                Assert.assertTrue(webElement.isSelected())
            } else (
                    Assert.assertFalse(webElement.isSelected())
            )
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    //A method that verify the attribute value
    @Given("^I (verify | assert) \"(.*)\" has \"(.*)\" attribute as \"(.*)\"\$")
    void verify_object_property(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToWebElement.class) WebElement webElement, String sAttribute, @Transform(TransformTextUsingYAML.class) String sExpectedValue) {
        try {
            String sActualText = webElement.getAttribute(sAttribute)
            Assert.assertEquals(sExpectedValue, sActualText == null ? "" : sActualText)
        } catch (AssertionError e) {
            if(bAssert)
                Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I (verify | assert) \"(.*)\" is displayed as \"(.*)\"\$")
    void verify_object_label(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToWebElement.class) WebElement webElement, String sExpectedValue) {
        try {
            Assert.assertEquals(webElement.getText(), sExpectedValue == null ? "" : sExpectedValue)
        } catch (AssertionError e) {
            if(bAssert)
                Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I (verify | assert) \"(.*)\" at position (\\d+) is displayed as \"(.*)\"\$")
    void verify_object_text(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToWebElements.class) List<WebElement> webElements, Integer i, @Transform(TransformTextUsingYAML.class) String sExpectedValue) {
            verify_object_label(bAssert, webElements.get(i - 1), sExpectedValue)
    }

    @Given("^I (verify | assert) \"(.*)\" at position (\\d+) is not displayed\$")
    void verify_object_not_displayed(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToWebElements.class) List<WebElement> webElements, Integer i) {
        try {
            webElements.get(i - 1)
            Context.getInstance().getReports().stepFail(e.getMessage())
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Given("^I click \"(.*)\" at position (\\d+)\$")
    void clickObjectUsingIndex(@Transform(TransformToWebElements.class) List<WebElement> webElements, Integer i) {
        webElements.get(i - 1).click()
    }

    @Given("^I navigate back\$")
    void backNavigation() {
        oDriver.navigate().back()
    }

    //UI Validation
    @Given("^I verify \"(.*)\" text color is \"(.*)\"\$")
    void verifyElementTextColor(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformToCssColor.class) String sColor) {
        verifyElementCssValue(webElement, )
    }




















}
