package learning.BDD.utilities.transformer

import cucumber.api.Transformer
import io.appium.java_client.MobileElement
import learning.BDD.utilities.Context

class TransformToMobileElements extends Transformer<List<MobileElement>> {
    @Override
    List<MobileElement> transform(String s) {
        return Context.getInstance().findMobileElements(s)
    }
}
