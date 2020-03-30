package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import org.apache.http.HttpHeaders

class HeaderSteps {

    @Given("^I set API Authorization header as \"(.*)\"\$")
    void setAuthorizationHeader(String sValue) {
        addAuthorizationHeader(sValue)
    }

    @Given("^I set API \"(.*)\" header as \"(.*)\"\$")
    void setHeader(String sHeader, String sValue) {
        addHeader(sHeader, sValue)
    }

    @Given("^I set API headers using below table\$")
    void setHeaders(DataTable table) {
        table.raw().each { row ->
            addHeader(row.get(0), row.get(1))
        }
    }

    @Given("^I (?:add|update) API Authorization header as \"(.*)\"\$")
    void addAuthorizationHeader(String sValue) {
        addHeader(HttpHeaders.AUTHORIZATION, Context.getInstance().getData(sValue))
    }

    @Given("^I (?:add|update) API header \"(.*)\" as \"(.*)\"\$")
    void addHeader(String sHeader, String sValue) {
        Context.getInstance().getHeaders().put(sHeader, Context.getInstance().getData(sValue))
    }

    @Given("^I clear API Headers\$")
    void clearAPIHeader() {
        Context.getInstance().clearHeaders()
    }

}
