package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context

class FormParametersSteps {

    @Given("^I (?:add/update) API form parameters \"(.*)\" as \"(.*)\"\$")
    void addFormParams(String sQuery, String sValue){
        Context.getInstance().getFormParams().put(sQuery, Context.getInstance().getData(sValue))
    }

    @Given("^I (?:add|update) API form parameters using below table\$")
    void addFormParams(DataTable table) {
        table.raw().each {row ->
            addFormParams(row.get(0), row.get(1))
        }
    }

    @Given("^I clear API form parameters\$")
    void clearFormParameters() {
        Context.getInstance().clearFormParams()
    }

}
