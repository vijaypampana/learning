package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import learning.BDD.utilities.util.DataUtil
import org.apache.commons.lang3.BooleanUtils

import static org.hamcrest.Matchers.*

class XMLSteps {

    @Given("^I set API XML root as \"(.*)\" and verify response using below table\$")
    void setRootPath(String sRootPath, DataTable table) {
        setRootPath(sRootPath)
        verifyXMLUsingTable(table)
    }

    @Given("^I set API XML root as \"(.*)\"\$")
    void setRootPath(String sRootPath) {
        Context.getInstance().getValidatableResponse().rootPath(sRootPath)
    }

    @Given("^I verify API XML response using below table\$")
    void verifyXMLUsingTable(DataTable table) {
        table.raw().each { row ->
            if(row.size()==3) {
                verifyXMLObjectEqualTo(row.get(0), row.get(1), row.get(3))
            } else {
                verifyXMLObjectEqualTo(row.get(0), row.get(1))
            }
        }
    }

    verifyXMLObjectEqualTo(String sQuery, String sExpectedValue, String sType) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, equalTo(JSONSteps.getJsonType(sExpectedValue, sType)))
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API XML element \"(.*)\" equal to \"(.*)\"\$")
    void verifyXMLObjectEqualTo(String sQuery, String sExpectedValue) {
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

    @Given("^I verify API XML element \"(.*)\" not equal to \"(.*)\"\$")
    void verifyXMLObjectNotEqualTo(String sQuery, String sExpectedValue) {
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

    @Given("^I store API XML element \"(.*)\" to \"(.*)\"\$")
    void storeXMLObject(String sQuery, String sMapKey) {
        sQuery = Context.getInstance().getData(sQuery)
        String sMapValue = Context.getInstance().getValidatableResponse().extract().body().xmlPath().getString(sQuery)
        Context.getInstance().addData(sMapKey, sMapValue)
    }

    static <T> List<T> getListByResponseKey(String sKey, String sQuery) {
        return DataUtil.retrieveValidatableResponse(sKey).extract().body().xmlPath().getString(sQuery)
    }

}
