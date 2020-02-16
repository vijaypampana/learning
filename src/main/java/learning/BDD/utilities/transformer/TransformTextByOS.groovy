package learning.BDD.utilities.transformer

import cucumber.api.Transformer

class TransformTextByOS extends Transformer<String> {
    @Override
    String transform(String s) {
        return Utility.getTextByOS(s)
    }
}
