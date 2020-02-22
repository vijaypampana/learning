package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import org.apache.commons.lang3.BooleanUtils

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not

class HTMLSteps {

    @Given("^I set API HTML root as \"(.*)\" and verify response using below table\$")
    void setRootPath(String sRootPath, DataTable table) {
        setRootPath(sRootPath)
        verifyHTMLUsingTable(table)
    }

    @Given("^I set API HTML root as \"(.*)\"\$")
    void setRootPath(String sRootPath) {
        Context.getInstance().getValidatableResponse().rootPath(sRootPath)
    }

    @Given("^I verify API HTML response using below table\$")
    void verifyHTMLUsingTable(DataTable table) {
        table.raw().each { row ->
            if(row.size()==3) {
                verifyHTMLObjectEqualTo(row.get(0), row.get(1), row.get(3))
            } else {
                verifyHTMLObjectEqualTo(row.get(0), row.get(1))
            }
        }
    }

    void verifyHTMLObjectEqualTo(String sQuery, String sExpectedValue, String sType) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, equalTo(JSONSteps.getJsonType(sExpectedValue, sType)))
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API HTML element \"(.*)\" equal to \"(.*)\"\$")
    void verifyHTMLObjectEqualTo(String sQuery, String sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            if(sExpectedValue.equalsIgnoreCase("NULL")) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(null))
            } else if (sExpectedValue.equalsIgnoreCase("EMPTY") || sExpectedValue.equalsIgnoreCase("\"\"")) {
                Context.getInstance().getValidatableResponse().body(sQuery+".isEmpty()", equalTo(true))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.TRUE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(true))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.FALSE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(false))
            } else {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(sExpectedValue))
            }
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API HTML element \"(.*)\" not equal to \"(.*)\"\$")
    void verifyHTMLObjectNotEqualTo(String sQuery, String sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            if(sExpectedValue.equalsIgnoreCase("NULL")) {
                Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(null)))
            } else if (sExpectedValue.equalsIgnoreCase("EMPTY") || sExpectedValue.equalsIgnoreCase("\"\"")) {
                Context.getInstance().getValidatableResponse().body(sQuery+".isEmpty()", not(equalTo(true)))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.TRUE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(false))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.FALSE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(true))
            } else {
                Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(sExpectedValue)))
            }
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I store API HTML element \"(.*)\" to \"(.*)\"\$")
    void storeHTMLObject(String sQuery, String sMapKey) {
        sQuery = Context.getInstance().getData(sQuery)
        String sMapValue = Context.getInstance().getValidatableResponse().extract().body().htmlPath().getString(sQuery)
        Context.getInstance().addData(sMapKey, sMapValue)
    }


}
