package learning.BDD.utilities.mobile

import cucumber.api.Transform
import cucumber.api.java.en.Given
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.TouchAction
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.pagefactory.AndroidFindBy
import io.appium.java_client.pagefactory.iOSXCUITFindBy
import io.appium.java_client.touch.WaitOptions
import io.appium.java_client.touch.offset.ElementOption
import io.appium.java_client.touch.offset.PointOption
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformTextByOS
import learning.BDD.utilities.transformer.TransformTextUsingYAML
import learning.BDD.utilities.transformer.TransformToAssert
import learning.BDD.utilities.transformer.TransformToMobileElement
import learning.BDD.utilities.transformer.TransformToMobileElements
import learning.BDD.utilities.utilEnum.XpathFormatter
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.ScreenOrientation
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Assert
import sun.security.util.PendingException

import java.time.Duration

class AppiumSteps {

    private AppiumDriver oDriver
    private WebDriverWait oWebDriverWait

    AppiumSteps() {
        if(oDriver == null) {
            oDriver = Context.getInstance().getAppiumDriver()
            oWebDriverWait = Context.getInstance().getWebDriverWait()
        }
    }

    //Methods for SET and CLEAR
    @Given("^I clear \"(.*)\"\$")
    void clear(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        mobileElement.clear();
    }

    @Given("^I enter \"(.*)\" as \"(.*)\"\$")
    void send_keys(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sText) {
        mobileElement.sendKeys(sText)
    }

    @Given("^I set \"(.*)\" as \"(.*)\"\$")
    void set_value(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        mobileElement.setValue(sValue)
    }

    @Given("^I clear & enter \"(.*)\" as \"(.*)\"\$")
    void clear_and_send_keys(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sText) {
        mobileElement.clear()
        mobileElement.sendKeys(sText)
    }

    @Given("^I clear & set \"(.*)\" as \"(.*)\"\$")
    void clear_and_set_value(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        mobileElement.clear()
        mobileElement.setValue(sValue)
    }

    @Given("^I (assert|verify) \"(.*)\" is displayed\$")
    void element_displayed(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        if(bAssert) {
            Assert.assertTrue(mobileElement.isDisplayed())
        } else {
            verifyDisplayed(mobileElement, true)
        }
    }

    @Given("^I (assert|verify) \"(.*)\" is not displayed\$")
    void element_not_displayed(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        if(bAssert) {
            Assert.assertFalse(mobileElement.isDisplayed())
        } else {
            verifyDisplayed(mobileElement, false)
        }
    }

    private void verifyDisplayed(String mobileElement, Boolean bExpected) {
        if(bExpected) {
            if(!Context.getInstance().findMobileElement(mobileElement)) {
                Context.getInstance().getReports().stepFail("Validation failure : MobileElement $mobileElement is not displayed")
            }
        } else {
            if(Context.getInstance().findMobileElement(mobileElement)) {
                Context.getInstance().getReports().stepFail("Validation failure : MobileElement $mobileElement is displayed")
            }
        }
    }

    @Given("^I verify \"(.*)\" is enabled\$")
    void verify_element_enabled(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        Assert.assertTrue(mobileElement.isEnabled())
    }

    @Given("^I verify \"(.*)\" is disabled\$")
    void verify_element_disabled(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        Assert.assertFalse(mobileElement.isEnabled())
    }

    @Given("^I verify \"(.*)\" is selected\$")
    void verify_selected(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        Assert.assertTrue(mobileElement.isSelected())
    }

    @Given("^I verify \"(.*)\" is not selected\$")
    void verify_not_selected(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        Assert.assertFalse(mobileElement.isSelected())
    }

    @Given("^I (verify | assert) \"(.*)\" has \"(.*)\" attribute as \"(.*)\"\$")
    void verify_object_property(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextByOS.class) String sAttribute,
                                @Transform(TransformTextUsingYAML.class) String sExpectedText) {
        try {
            Assert.assertEquals(mobileElement.getAttribute(sAttribute), sExpectedText )
        } catch (AssertionError e) {
            if(bAssert) {
                Context.getInstance().getReports().stepFail("")
            } else {
                Context.getInstance().getReports().stepError("")
            }
        }
    }

    @Given("^I (verify | assert) \"(.*)\" is displayed as \"(.*)\"\$")
    void verify_object_text(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sExpectedText) {
        String sActualText = "", sAttribute = "text"
        if(oDriver instanceof IOSDriver) {
            List<String> typeThatUseValue = Arrays.asList("XCUIElementTypeTextField", "XCUIElementTypeSecureTextField", "XCUIElementTypePickerWheel")
            if(typeThatUseValue.contains(mobileElement.getAttribute("type"))) {
                sAttribute = "value"
            } else {
                sAttribute = "label"
            }
        }

        sActualText = mobileElement.getAttribute(sAttribute)
        try {
            Assert.assertEquals(sExpectedText, sActualText == null ? "" : sActualText)
        } catch (AssertionError e) {
            if(bAssert) {
                Context.getInstance().getReports().stepFail("")
            } else {
                Context.getInstance().getReports().stepError(e.getMessage())
            }
        }
    }

    @Given("^I (verify | assert) \"(.*)\" at position (\\d+) is displayed as \"(.*)\"\$")
    void verify_object_text(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToMobileElements.class) List<MobileElement> mobileElements, Integer i, @Transform(TransformTextUsingYAML.class) String sExpectedText) {
        verify_object_text(bAssert, mobileElements.get(i - 1), sExpectedText)
    }

    @Given("^I (verify | assert) \"(.*)\" at position (\\d+) is NOT displayed\$")
    void verify_object_not_displayed(@Transform(TransformToAssert.class) Boolean bAssert, @Transform(TransformToMobileElements.class) List<MobileElement> mobileElements, Integer i) {
        try {
            mobileElements.get(i - 1)
            Context.getInstance().getReports().stepFail("Element is still displayed")
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Given("^I click \"(.*)\" at position (\\d+)\$")
    void clickObjectUsingIndex(@Transform(TransformToMobileElements.class) List<MobileElement> mobileElements, int i) {
        mobileElements.get(i - 1).click()
    }

    //Generic Verifications
    @Given("^I verify \"(.*)\" button is displayed\$")
    void verify_button_with_label(@Transform(TransformTextUsingYAML.class) String sValue) {
        verify_element_with_type_and_text("button", "label", sValue, true)
    }

    @Given("^I verify \"(.*)\" button is NOT displayed\$")
    void verify_button_with_label_negative(@Transform(TransformTextUsingYAML.class) String sValue) {
        verify_element_with_type_and_text("button", "label", sValue, false)
    }

    @Given("^I verify \"(.*)\" text is displayed\$")
    void verify_text_with_label(@Transform(TransformTextUsingYAML.class) String sValue) {
        verify_element_with_type_and_text("text", "label", sValue, true)
    }

    @Given("^I verify \"(.*)\" text is NOT displayed\$")
    void verify_text_with_label_negative(@Transform(TransformTextUsingYAML.class) String sValue) {
        verify_element_with_type_and_text("text", "label", sValue, false)
    }

    @Given("^I verify text contains \"(.*)\" is displayed\$")
    void verify_contains_text_with_label(String sValue) {
        verify_element_with_type_and_text("containstext", "label", sValue, true)
    }

    @Given("^I verify text contains \"(.*)\" is NOT displayed\$")
    void verify_contains_text_with_label_negative(String sValue) {
        verify_element_with_type_and_text("containstext", "label", sValue, false)
    }

    void verify_element_with_type_and_text(String sElementType, String sAttribute, String sValue, Boolean bDisplayFlag) {
        try {
            String sxPath = ""
            switch (sElementType + "-" + sAttribute) {
                case "button-label":
                    sxPath = XpathFormatter.LABELVALIDATIONFORMATTER.BUTTONEQUALS.getXpath(sValue)
                    break
                case "text-label":
                    sxPath = XpathFormatter.LABELVALIDATIONFORMATTER.TEXTEQUALS.getXpath(sValue)
                    break
                case "containstext-label":
                    sxPath = XpathFormatter.LABELVALIDATIONFORMATTER.TEXTCONTAINS.getXpath(sValue)
                    break
                default:
                    throw new IllegalArgumentException("Switch case arguments for verify_element_with_type_and_text method fails")
            }

            MobileElement oElement = (MobileElement) oWebDriverWait.until(ExpectedConditions.visibilityOf(Context.getInstance().findMobileElement(By.xpath(sxPath))))

            if(bDisplayFlag) {
                Assert.assertTrue(oElement.isDisplayed())
            } else {
                Assert.assertFalse(oElement.isDisplayed())
            }
        } catch (Exception e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    //Methods for Typing or KeyBoard Operations
    @Given("^I type test as \"(.*)\"\$")
    void type_text(@Transform(TransformTextUsingYAML.class) String sText) {
        oDriver.getKeyboard().sendKeys(sText)
    }

    @Given("^I type \"(.*)\" in \"(.*)\"\$")
    void type_text_on_element(@Transform(TransformTextUsingYAML.class) String sText, @Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        mobileElement.click()
        type_text(sText)
    }

    @Given("^I type text as \"(.*)\" one character at a time\$")
    void type_text_one_character_at_a_time(@Transform(TransformTextUsingYAML.class) String sText) {
        for(int i=0; i < sText.length(); i++) {
            oDriver.getKeyboard().sendKeys(Character.toString(sText.charAt(i)))
        }
    }

    @Given("^I type \"(.*)\" in \"(.*)\" one character at a time\$")
    void type_text_on_element_one_character_at_a_time(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sText) {
        mobileElement.click()
        type_text_one_character_at_a_time(sText)
    }

    //TBD
    @Given("^I press \"(.*)\" key\$")
    void press_key_sequence(String sKeySequence) throws Throwable {
        throw new PendingException()
    }

    //Switching Context NATIVE, WEB, VISUAL
    private void switch_context(String sContext) {
        oDriver.context(sContext)
    }

    @Given("^I switch context to NATIVE\$")
    void switch_context_to_native() {
        switch_context("NATIVE_APP")
    }

    @Given("^I switch context to WEB\$")
    void switch_context_to_WEB() {
        switch_context("WEBVIEW_1")
    }

    @Given("^I switch context to WEB (\\d+)\$")
    void switch_context_to_web(Integer i) {
        switch_context("WEBVIEW_" + i)
    }

    //TBD
    @Given("^I switch context to VISUAL\$")
    void switch_context_to_visual() throws Throwable {
        throw new PendingException()
    }

    //APP Operations
    @Given("^I launch application\$")
    void launch_app() {
        oDriver.launchApp()
    }

    @Given("^I close application\$")
    void close_app() {
        oDriver.closeApp()
    }

    @Given("^I relaunch application\$")
    void relaunch_app() {
        oDriver.closeApp()
        oDriver.launchApp()
    }

    @Given("^I clean application\$")
    void clean_app() {
        oDriver.resetApp()
    }

    @Given("^I install \"(.*)\" application\$")
    void install_app(@Transform(TransformTextByOS.class) String sPath) {
        oDriver.installApp(sPath)
    }

    //TBD
    @Given("^I install \"(.*)\" application instrumented\$")
    void install_app_instrumented(String sAppName) {

    }

    @Given("^I uninstall \"(.*)\" application\$")
    void uninstall_app(String sBundleId) {
        oDriver.removeApp(sBundleId)
    }

    //TBD
    @Given("^I reset all application\$")
    void reset_apps() {
        //
    }

    //TBD
    @Given("^I launch \"(.*)\" application\$")
    void launch_app(String sAppName) {

    }

    //TBD
    @Given("^I close \"(.*)\" application\$")
    void close_app(String sAppName) {

    }

    //TBD
    @Given("^I relaunch \"(.*)\" application\$")
    void relaunch_app(String sAppName) {

    }

    //TBD
    @Given("^I clean \"(.*)\" application\$")
    void clean_app(String sAppName) {

    }

    //Device Operations
    @Given("^I press home button\$")
    void home() {
        if(!Context.getInstance().isbIOS()) {
            oDriver.getKeyboard().pressKey(new KeyEvent(AndroidKey.HOME))
        } else {
            oDriver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"))
        }
    }

    //TBD
    @Given("^I close device\$")
    void close_device() {

    }

    @Given("^I rotate device\$")
    void rotate_device() {
        if(oDriver.getOrientation().equals(ScreenOrientation.PORTRAIT)) {
            oDriver.rotate(ScreenOrientation.LANDSCAPE)
        } else {
            oDriver.rotate(ScreenOrientation.PORTRAIT)
        }
    }

    //Key Board Operations
    @Given("^I hide keyboard\$")
    void hide_keyboard() {
        if(oDriver instanceof AndroidDriver) {
            oDriver.hideKeyboard()
        } else {
            try {
                oDriver.findElement(By.xpath("//XCUIElementTypeOther//XCUIElementTypeButton[@label=\"Done\"]")).click()
            } catch (Exception e) {

            }
        }
    }

    //Swipe
    void swipeByPercentage(int iX1, int iY1, int iX2, int iY2) {
        Dimension size = oDriver.manage().window().getSize()
        iX1 = (size.width * iX1) / 100
        iY1 = (size.height * iY1) / 100
        iX2 = (size.width * iX2) / 100
        iY2 = (size.height * iY2) / 100

        if(oDriver instanceof AndroidDriver) {
            swipe(iX1, iY1, iX2, iY2)
        } else {
            swipe(iX1, iY1, iX2 - iX1, iY2 - iY1)
        }
    }

    void swipe(int iStartX, int iStartY, int iEndX, int iEndY) {
        TouchAction oAction = new TouchAction(oDriver)
            .press(PointOption.point(iStartX, iStartY))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
            .moveTo(PointOption.point(iEndX, iEndY))
            .release()
        oAction.perform()
    }

    static void swipe(MobileElement oStart, MobileElement oEnd) {
        AppiumDriver oDriver = Context.getInstance().getAppiumDriver()
        TouchAction oAction = new TouchAction(oDriver)
        if(oDriver instanceof AndroidDriver) {
            oAction.longPress(ElementOption.element(oStart)).moveTo(ElementOption.element(oEnd)).release()
        } else {
            oAction.press(ElementOption.element(oStart)).moveTo(ElementOption.element(oEnd)).release()
        }
        oDriver.performTouchAction(oAction)
    }

    @Given("^I swipe until \"(.*)\" element visible\$")
    void swipe_until_element_visible(String sObject) {
        Boolean bVisible = false
        int i = 0
        while((!bVisible) && (i <= 5)) {
            i++
            try {
                Context.getInstance().findMobileElement(sObject).isDisplayed()
                bVisible = true
            } catch (Exception e) {
                swipeByPercentage(50, 60, 50, 40)
            }
        }
    }

    @Given("^I swipe until \"(.*)\" text visible\$")
    void swipe_until_text_visible(String sObject) {
        Boolean bVisible = false
        int i = 0
        String sXpath = XpathFormatter.LABELVALIDATIONFORMATTER.TEXTCONTAINS.getXpath(sObject)
        while((!bVisible) && (i <= 5)) {
            i++
            try {
                Context.getInstance().getWebDriverShortWait().until(ExpectedConditions.visibilityOf(Context.getInstance().findMobileElement(By.xpath(sXpath)))).isDisplayed()
                bVisible = true
            } catch (Exception e) {
                swipeByPercentage(50, 60, 50, 40)
            }
        }
    }

    @Given("^I swipe up\$")
    void swipe_up() {
        if(oDriver instanceof AndroidDriver) {
            swipe(50, 80, 50, 20)
        } else {
            swipe(50, 80, 0, -60)
        }
    }

    @Given("^I swipe down\$")
    void swipe_down() {
        if(oDriver instanceof AndroidDriver) {
            swipe(50, 20, 50, 80)
        } else {
            swipe(50, 20, 0, 60)
        }
    }

    @Given("^I swipe left\$")
    void swipe_left() {
        if(oDriver instanceof AndroidDriver) {
            swipe(80, 50, 20, 50)
        } else {
            swipe(80, 50, -60, 0)
        }
    }

    @Given("^I swipe right\$")
    void swipe_right() {
        if(oDriver instanceof AndroidDriver) {
            swipe(20, 50, 80, 50)
        } else {
            swipe(20, 50, 60, 0)
        }
    }

    @Given("^I select \"(.*)\" from the drop down\$")
    void select_list_from_drop_down(@Transform(TransformTextUsingYAML.class) String sValue) {
        if(oDriver instanceof IOSDriver) {
            Context.getInstance().findMobileElement(By.xpath("//XCUIElementTypePickerWheel")).sendKeys(sValue)
            Context.getInstance().findMobileElement(By.xpath("//XCUIElementTypeButton[@label=\"Done\"]")).click()
        } else {
            Context.getInstance().findMobileElement(By.xpath("//android.widget.CheckedTextView[@text=\"$sValue\"]")).click()
        }
    }

    //Methods for TAP or CLICK Options
    @Given("^I tap on \"(.*)\"\$")
    void click(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        mobileElement.click()
    }

    private void tap_by_percentage(int x, int y) {
        try {
            TouchAction oAction = new TouchAction(oDriver)
            Dimension size = oDriver.manage().window().getSize()
            x = (size.width * x) / 100
            y = (size.height * y) / 100
            oAction.tap(PointOption.point(x, y))
            oAction.perform()
        } catch (Exception e) {
        }
    }

    @Given("^I long press on \"(.*)\"\$")
    void long_press_on_element(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        TouchAction oAction = new TouchAction(oDriver)
        oAction.longPress(ElementOption.element(mobileElement))
        oAction.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
        oAction.perform()
    }

    //Generic Tap Methods
    @Given("^I tap on \"(.*)\" button\$")
    void click_on_button_with_text(@Transform(TransformTextUsingYAML.class) String sValue) {
        click_element_with_type_and_text("button", "label", sValue)
    }

    @Given("^I tap on \"(.*)\" text\$")
    void click_on_element_with_text(@Transform(TransformTextUsingYAML.class) String sValue) {
        click_element_with_type_and_text("text", "label", sValue)
    }

    @Given("^I tap on \"(.*)\" contains text\$")
    void click_on_element_contains_text(@Transform(TransformTextUsingYAML.class) String sValue) {
        click_element_with_type_and_text("containstext", "label", sValue)
    }

    @Given("^I verify \"(.*)\" contains text as \"(.*)\"\$")
    void verify_object_partial_content(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        try {
            Assert.assertTrue(mobileElement.getText().contains(sValue))
        } catch(AssertionError e) {
            Context.getInstance().getReports().stepFail()
        }
    }

    private void click_element_with_type_and_text(String sElementType, String sAttribute, String sValue) {
        String sXpath = ""
        switch(sElementType + "-" + sAttribute) {
            case "button-label":
                sXpath = XpathFormatter.LABELVALIDATIONFORMATTER.BUTTONEQUALS.getXpath(sValue)
                break
            case "text-label":
                sXpath = XpathFormatter.LABELVALIDATIONFORMATTER.TEXTEQUALS.getXpath(sValue)
                break
            case "containstext-label":
                sXpath = XpathFormatter.LABELVALIDATIONFORMATTER.TEXTCONTAINS.getXpath(sValue)
                break
        }
        oWebDriverWait.until(ExpectedConditions.visibilityOf(Context.getInstance().findMobileElement(By.xpath(sXpath)))).click()
    }

    // Wait Methods
    @Given("^I wait for \"(.*)\" to be visible\$")
    void wait_for_visibility(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        oWebDriverWait.until(ExpectedConditions.visibilityOf(mobileElement))
    }

    @Given("^I wait for \"(.*)\" to be invisible\$")
    void wait_for_invisibility(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        oWebDriverWait.until(ExpectedConditions.invisibilityOf(mobileElement))
    }

    @Given("^I wait for \"(.*)\" to be clickable\$")
    void wait_for_clickable(@Transform(TransformToMobileElement.class) MobileElement mobileElement) {
        oWebDriverWait.until(ExpectedConditions.elementToBeClickable(mobileElement))
    }

    @Given("^I wait for \"(.*)\" to have text as \"(.*)\"\$")
    void wait_for_element_with_text(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        oWebDriverWait.until(ExpectedConditions.textToBePresentInElement(mobileElement, sValue))
    }

    @Given("^I wait for \"(.*)\" to have value as \"(.*)\"\$")
    void wait_for_element_with_value(@Transform(TransformToMobileElement.class) MobileElement mobileElement, @Transform(TransformTextUsingYAML.class) String sValue) {
        oWebDriverWait.until(ExpectedConditions.textToBePresentInElementValue(mobileElement, sValue))
    }

    @Given("^I verify \"(.*)\" exactly overlaps \"(.*)\"\$")
    public void verifyElementPositionExactlyOverlap(@Transform(TransformToMobileElement.class) WebElement webElementOne, @Transform(TransformToMobileElement.class) WebElement webElementTwo) {
        if(!(webElementOne.getAttribute("bounds").equals(webElementTwo.getAttribute("bounds")))) {
            Context.getInstance().getReports().stepFail("");
        }
    }

    @iOSXCUITFindBy()
    @AndroidFindBy()
    public WebElement testElementc













}
