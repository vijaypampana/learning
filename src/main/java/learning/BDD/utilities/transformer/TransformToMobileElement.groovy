package learning.BDD.utilities.transformer

import cucumber.api.Transformer
import io.appium.java_client.MobileElement
import learning.BDD.utilities.Context
import org.openqa.selenium.WebElement

class TransformToMobileElement extends Transformer<MobileElement> {
    @Override
    MobileElement transform(String s) {
        return Context.getInstance().findMobileElement(s)
    }
}
