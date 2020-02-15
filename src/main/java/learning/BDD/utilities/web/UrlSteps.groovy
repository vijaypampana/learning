package learning.BDD.utilities.web

import cucumber.api.Transform
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import learning.BDD.utilities.transformer.TransformToAssert
import learning.BDD.utilities.util.CompareUtil
import org.openqa.selenium.WebDriver

class UrlSteps {

    private WebDriver oDriver

    UrlSteps() {
        if(oDriver == null) {
            oDriver = Context.getInstance().getWebDriver()
        }
    }

    @Given("^I validate browser url \"(.*)\"\$")
    void verifyUrl(String sExpectedUrl) {
        verifyUrlEqualTo(false, sExpectedUrl)
    }

    @Given("^I (verify | assert) URL equal to \"(.*)\"\$")
    void verifyUrlEqualTo(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.equalTo(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }

    @Given("^I (verify | assert) URL equal to \"(.*)\" by ignoring case\$")
    void verifyUrlEqualToIgnoreCase(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.equalToIgnoreCase(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }

    @Given("^I (verify | assert) URL not equal to \"(.*)\"\$")
    void verifyUrlNotEqualTo(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.notEqualTo(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }

    @Given("^I (verify | assert) URL contains \"(.*)\"\$")
    void verifyUrlContains(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.contains(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }

    @Given("^I (verify | assert) URL doesn't contains \"(.*)\"\$")
    void verifyUrlNotContains(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.notContains(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }

    @Given("^I (verify | assert) URL starts with \"(.*)\"\$")
    void verifyUrlStartsWith(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.startsWith(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }

    @Given("^I (verify | assert) URL ends with \"(.*)\"\$")
    void verifyUrlEndsWith(@Transform(TransformToAssert.class) Boolean bAssert, String sExpectedUrl) {
        CompareUtil.endsWith(oDriver.getCurrentUrl(), sExpectedUrl, bAssert)
    }
}
