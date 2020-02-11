package learning.BDD.utilities.transformer

import cucumber.api.Transformer
import learning.BDD.utilities.Context

class TransformTextUsingYAML extends Transformer<String> {

    @Override
    String transform(String sValue) {
        Context.getInstance().getData(sValue)
    }
}
