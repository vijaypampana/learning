package learning.BDD.utilities.web

import cucumber.api.DataTable
import cucumber.api.Transform
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformTextUsingYAML
import learning.BDD.utilities.transformer.TransformToAssert
import learning.BDD.utilities.transformer.TransformToCssColor
import learning.BDD.utilities.transformer.TransformToWebElement
import learning.BDD.utilities.transformer.TransformToWebElements
import learning.BDD.utilities.utilEnum.CssProperty
import learning.BDD.utilities.utilEnum.Position
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Point
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Action
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Assert

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
        verifyElementCssValue(webElement, CssProperty.COLOR.getProperty(), sColor)
    }

    @Given("^I verify \"(.*)\" background color is \"(.*)\"\$")
    void verifyElementBackgroundColor(@Transform(TransformToWebElement.class) WebElement webElement, @Transform(TransformToCssColor.class) String sColor) {
        verifyElementCssValue(webElement, CssProperty.BACKGROUND_COLOR.getProperty(), sColor)
    }

    @Given("^I verify \"(.*)\" font size is \"(.*)\"\$")
    void verifyElementFontSize(@Transform(TransformToWebElement.class) WebElement webElement, String sSize) {
        verifyElementCssValue(webElement, CssProperty.FONT_SIZE.getProperty(), sSize)
    }

    @Given("^I verify \"(.*)\" text is align \"(.*)\"\$")
    void verifyElementTextAlignment(@Transform(TransformToWebElement.class) WebElement webElement, String sAlignment) {
        verifyElementCssValue(webElement, CssProperty.TEXT_ALIGN.getProperty(), sAlignment)
    }

    @Given("^I verify \"(.*)\" has CSS property \"(.*)\" with value \"(.*)\"\$")
    void verifyElementCssValue(@Transform(TransformToWebElement.class) WebElement webElement, String sProperty, String sExpectedValue) {
        String sActualValue = webElement.getCssValue(sProperty)
        if(!sActualValue.equalsIgnoreCase(sExpectedValue)) {
            Context.getInstance().getReports().stepFail("Css property did not match")
        }
    }

    //Position Validations
    @Given("^I verify \"(.*)\" is displayed anywhere left of \"(.*)\"\$")
    void verifyElementPositionLeft(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.LEFT)
    }

    @Given("^I verify \"(.*)\" is displayed exactly left of \"(.*)\"\$")
    void verifyElementPositionExactlyLeft(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.EXACTLY_LEFT)
    }

    @Given("^I verify \"(.*)\" is displayed anywhere right of \"(.*)\"\$")
    void verifyElementPositionRight(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.RIGHT)
    }

    @Given("^I verify \"(.*)\" is displayed exactly right of \"(.*)\"\$")
    void verifyElementPositionExactlyRight(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.EXACTLY_RIGHT)
    }

    @Given("^I verify \"(.*)\" is displayed anywhere above of \"(.*)\"\$")
    void verifyElementPositionTop(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.TOP)
    }

    @Given("^I verify \"(.*)\" is displayed exactly above of \"(.*)\"\$")
    void verifyElementPositionExactlyTop(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.EXACTLY_TOP)
    }

    @Given("^I verify \"(.*)\" is displayed anywhere below of \"(.*)\"\$")
    void verifyElementPositionBottom(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.BOTTOM)
    }

    @Given("^I verify \"(.*)\" is displayed exactly below of \"(.*)\"\$")
    void verifyElementPositionExactlyBottom(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        verifyElementPosition(webElementOne.getLocation(), webElementTwo.getLocation(), Position.EXACTLY_BOTTOM)
    }

    @Given("I verify \"(.*)\" is displayed inside \"(.*)\"")
    void verifyElementPositionInside(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        Point pointOne = webElementOne.getLocation()
        Point pointTwo = webElementTwo.getLocation()

        Dimension dimensionOne = webElementOne.getSize()
        Dimension dimensionTwo = webElementTwo.getSize()

        if((pointOne.getX() >= pointTwo.getX()) &&
                (pointOne.getX() + dimensionOne.getWidth() <= pointTwo.getX() + dimensionTwo.getWidth()) &&
                (pointOne.getY() >= pointTwo.getY()) &&
                (pointOne.getY() + dimensionOne.getHeight() <= pointTwo.getY() + dimensionTwo.getWidth())
            ) {
        } else {
            Context.getInstance().getReports().stepFail()
        }
    }

    @Given("^I verify \"(.*)\" is displayed outside \"(.*)\"\$")
    void verifyElementPositionOutside(@Transform(TransformToWebElement.class) WebElement webElementOne, @Transform(TransformToWebElement.class) WebElement webElementTwo) {
        Point pointOne = webElementOne.getLocation()
        Point pointTwo = webElementTwo.getLocation()

        Dimension dimensionOne = webElementOne.getSize()
        Dimension dimensionTwo = webElementTwo.getSize()

        if((pointOne.getX() > pointTwo.getX() || pointOne.getX() < pointTwo.getX()) &&
                (pointOne.getX() + dimensionOne.getWidth() > pointTwo.getX() + dimensionTwo.getWidth() || pointOne.getX() + dimensionOne.getWidth() < pointTwo.getX() + dimensionTwo.getWidth()) &&
                (pointOne.getY() > pointTwo.getY() || pointOne.getY() < pointTwo.getY()) &&
                (pointOne.getY() + dimensionOne.getHeight() > pointTwo.getY() + dimensionTwo.getHeight() || pointOne.getY() + dimensionOne.getHeight() < pointTwo.getY() + dimensionTwo.getHeight())
        ) {

        } else {
            Context.getInstance().getReports().stepFail()
        }
    }

    //Pointtwo is reference and we are searching for point one location
    private void verifyElementPosition(Point pointOne, Point pointTwo, Position direction) {
        boolean bVerify = false

        switch (direction) {
            case "RIGHT":
                bVerify = pointOne.getX() > pointTwo.getX()
                break
            case "EXACTLY_RIGHT":
                bVerify = pointOne.getX() > pointTwo.getX()  && pointOne.getY() == pointTwo.getY()
                break
            case "LEFT":
                bVerify = pointOne.getX() < pointTwo.getX()
                break
            case "EXACTLY_LEFT":
                bVerify = pointOne.getX() < pointTwo.getX()  && pointOne.getY() == pointTwo.getY()
                break
            case "TOP":
                bVerify = pointOne.getY() < pointTwo.getY()
                break
            case "EXACTLY_TOP":
                bVerify = pointOne.getY() < pointTwo.getY()  && pointOne.getX() == pointTwo.getX()
                break
            case "BOTTOM":
                bVerify = pointOne.getY() > pointTwo.getY()
                break
            case "EXACTLY_BOTTOM":
                bVerify = pointOne.getY() > pointTwo.getY()  && pointOne.getX() == pointTwo.getX()
                break
        }

        if(!bVerify) {
            Context.getInstance().getReports().stepFail("")
        }

    }

    @Given("^I mousehover on \"(.*)\"\$")
    void mouseHover(@Transform(TransformToWebElement.class) WebElement webElement) {
        Actions actions = new Actions(oDriver)
        actions.moveToElement(webElement).build().perform()
    }

    @Given("^I move \"(.*)\" slider horizontally to \\d+ percent\$")
    void moveSliderHorizontally(@Transform(TransformToWebElement.class) WebElement webElement, int iPercentage) {
        Actions actions = new Actions(oDriver)
        actions.moveToElement(webElement, (webElement.getSize().getWidth() * iPercentage) / 100, 0).click().build().perform()
    }

    @Given("^I move \"(.*)\" slider vertically to \\d+ percent\$")
    void moveSliderVertically(@Transform(TransformToWebElement.class) WebElement webElement, int iPercentage) {
        Actions actions = new Actions(oDriver)
        actions.moveToElement(webElement, 0, (webElement.getSize().getHeight() * iPercentage) / 100).click().build().perform()
    }

    @Given("I validate \"(.*)\" table using below values")
    void validateTableData(@Transform(TransformToWebElement.class) WebElement webTable, DataTable table) {
        try {
            if(element_displayed(true, webTable)) {
                Assert.assertEquals(processWebTable(webTable).body().html(), frameExpectedTable(table).body().html())
            } else {
                Context.getInstance().getReports().stepFail("Table is not displayed")
            }
        } catch (Exception e) {
            Context.getInstance().getReports().stepFail("Table Validation failed : " + e.getMessage())
        }
    }

    private Document processWebTable(WebElement webTable) {
        Document doc = Jsoup.parse(
                Jsoup.clean(webTable.getAttribute("outerHTML").replace("&nbsp;", ""),
                                new Whitelist().addTags("table", "tbody", "td", "tfoot", "th", "thead", "tr")))
        doc.outputSettings().indentAmount(0).prettyPrint(false)
        trimHTMLnodeValue(doc, "td")
        trimHTMLnodeValue(doc, "th")
        return doc
    }

    private Document frameExpectedTable(DataTable table) {
        String sExpRow = "", sExpectedHeader = "", sExpectedColumn = "", sExpBody = "", sExpectedTable = ""
        table.raw().each { row ->
            row.each { data ->
                if(!Context.getInstance().getData(data).isEmpty()) {
                    if(Context.getInstance().getData(data).equalsIgnoreCase("header")) {
                        sExpectedHeader += "<th>" + Context.getInstance().getData(data) + "</th>"
                    } else {
                        sExpectedColumn += "<td>" + Context.getInstance().getData(data) + "</td>"
                    }
                }
            }
            sExpRow += (!sExpectedColumn.isEmpty()) ? "<tr>" + sExpectedColumn + "</tr>" : ""
            sExpectedColumn = ""
        }
        sExpectedHeader = (!sExpectedHeader.isEmpty()) ? "<thead>" + sExpectedHeader + "</thead>" : ""
        sExpBody = (!sExpRow.isEmpty()) ? "<tbody>" + sExpRow + "</tbody>" : ""
        sExpectedTable = "<table>" + sExpectedHeader + sExpBody + "</table>"
        return Jsoup.parse(sExpectedTable)
    }

    private void trimHTMLnodeValue(Document doc, String tagName) {
        Elements tags = doc.select(tagName)
        tags.each { tag ->
            tag.textNodes().each { node ->
                String tagText = node.text().trim()
                if(tagText.trim().length() > 0) {
                    node.text(tagText)
                }
            }
        }
    }
}
