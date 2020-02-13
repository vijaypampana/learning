package learning.BDD.utilities.transformer

import cucumber.api.Transformer
import learning.BDD.utilities.Context
import org.openqa.selenium.WebElement

class TransformToWebElement extends Transformer<WebElement> {
    @Override
    WebElement transform(String s) {
        return Context.getInstance().findElement(s)
    }
}
