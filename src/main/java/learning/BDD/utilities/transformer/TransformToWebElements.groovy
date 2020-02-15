package learning.BDD.utilities.transformer

import cucumber.api.Transformer
import learning.BDD.utilities.Context
import org.openqa.selenium.WebElement

class TransformToWebElements extends Transformer<List<WebElement>> {
    @Override
    List<WebElement> transform(String s) {
        return Context.getInstance().findElements(s)
    }
}
