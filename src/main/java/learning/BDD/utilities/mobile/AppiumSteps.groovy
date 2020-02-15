package learning.BDD.utilities.mobile

import cucumber.api.Transform
import cucumber.api.java.en.Given
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformTextUsingYAML
import learning.BDD.utilities.transformer.TransformToAssert
import learning.BDD.utilities.transformer.TransformToMobileElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.Assert

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

    @Given("I clear & set \"(.*)\" as \"(.*)\"")
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

























}
