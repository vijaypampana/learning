package learning.BDD.utilities.transformer

import cucumber.api.Transformer

class TransformToAssert extends Transformer<Boolean> {
    @Override
    Boolean transform(String s) {
        return s.equalsIgnoreCase("ASSERT") ? true : false
    }
}
