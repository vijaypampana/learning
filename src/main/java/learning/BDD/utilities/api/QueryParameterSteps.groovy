package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context

class QueryParameterSteps {

    @Given("^I (?:add|update) API query parameters \"(.*)\" as \"(.*)\"\$")
    void addQueryParams(String sQuery, String sValue) {
        Context.getInstance().getParams().put(sQuery, Context.getInstance().getData(sValue))
    }

    @Given("^I (?:add|update) API query parameters using below table\$")
    void addQueryParams(DataTable table) {
        table.raw().each { row ->
            addQueryParams(row.get(0), row.get(1))
        }
    }

    @Given("^I clear API Query Parameters\$")
    void clearQueryParams() {
        Context.getInstance().clearQueryParams()
    }

}
