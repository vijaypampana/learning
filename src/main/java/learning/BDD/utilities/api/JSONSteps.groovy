package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.math.NumberUtils

import static org.hamcrest.Matchers.*

class JSONSteps {

    @Given("^I set API JSON root as \"(.*)\" and verify response using below table\$")
    void setRootPath(String sRootPath, DataTable table) {
        setRootPath(sRootPath)
        verifyJsonUsingTable(table)
    }

    @Given("^I set API JSON root as \"(.*)\"\$")
    void setRootPath(String sRootPath) {
        Context.getInstance().getValidatableResponse().rootPath(sRootPath)
    }

    @Given("^I verify API JSON response using below table\$")
    void verifyJsonUsingTable(DataTable table) {
        table.raw().each { row ->
            if(row.size()==3) {
                verifyJsonObjectEqualto(row.get(0), row.get(1), row.get(2))
            } else {
                verifyJsonObjectEqualto(row.get(0), row.get(1))
            }
        }
    }

    void verifyJsonObjectEqualto(String sQuery, String sExpectedValue, String type) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, equalTo(getJsonType(sExpectedValue, type)))
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API JSON Object \"(.*)\" equal to \"(.*)\"\$")
    void verifyJsonObjectEqualTo(String sQuery, String sExpectedValue) {
        sQuery = Context.getInstance().getData(sQuery)
        sExpectedValue = Context.getInstance().getData(sExpectedValue)

        try {
            if(sExpectedValue.equalsIgnoreCase("NULL")) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(null))
            } else if (sExpectedValue.equalsIgnoreCase("EMPTY") || sExpectedValue.equalsIgnoreCase("{}")) {
                Context.getInstance().getValidatableResponse().body(sQuery+".isEmpty()", equalTo(true))
            } else if ((sExpectedValue.startsWith("\"") && sExpectedValue.endsWith("\"")) || (sExpectedValue.startsWith("\'") && sExpectedValue.endsWith("\'"))) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(sExpectedValue.substring(1, sExpectedValue.length() - 1)))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.TRUE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(true))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.FALSE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(false))
            } else if (NumberUtils.isCreatable(sExpectedValue)) {
                try {
                    Context.getInstance().getValidatableResponse().body(sQuery, equalTo(Integer.parseInt(sExpectedValue)))
                } catch (NumberFormatException e) {
                    Context.getInstance().getValidatableResponse().body(sQuery, equalTo(Double.parseDouble(sExpectedValue)))
                }
            } else {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(sExpectedValue))
            }

        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API JSON Object \"(.*)\" not equal to \"(.*)\"\$")
    void verifyJsonObjectNotEqualTo(String sQuery, String sExpectedValue) {
        sQuery = Context.getInstance().getData(sQuery)
        sExpectedValue = Context.getInstance().getData(sExpectedValue)

        try {
            if(sExpectedValue.equalsIgnoreCase("NULL")) {
                Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(null)))
            } else if (sExpectedValue.equalsIgnoreCase("EMPTY") || sExpectedValue.equalsIgnoreCase("{}")) {
                Context.getInstance().getValidatableResponse().body(sQuery+".isEmpty()", equalTo(false))
            } else if ((sExpectedValue.startsWith("\"") && sExpectedValue.endsWith("\"")) || (sExpectedValue.startsWith("\'") && sExpectedValue.endsWith("\'"))) {
                Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(sExpectedValue.substring(1, sExpectedValue.length() - 1))))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.TRUE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(false))
            } else if (BooleanUtils.toBooleanObject(sExpectedValue) == Boolean.FALSE) {
                Context.getInstance().getValidatableResponse().body(sQuery, equalTo(true))
            } else if (NumberUtils.isCreatable(sExpectedValue)) {
                try {
                    Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(Integer.parseInt(sExpectedValue))))
                } catch (NumberFormatException e) {
                    Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(Double.parseDouble(sExpectedValue))))
                }
            } else {
                Context.getInstance().getValidatableResponse().body(sQuery, not(equalTo(sExpectedValue)))
            }

        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API Collection \"(.*)\" has size \"(.*)\"\$")
    void verifyJSONCollectionHasSize(String sQuery, Integer sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            Context.getInstance().getValidatableResponse().body(sQuery, hasSize(sExpectedValue))
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API Collection \"(.*)\" has item \"(.*)\"\$")
    void verifyJSONCollectionHasItem(String sQuery, String sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, everyItem(hasItem(sExpectedValue)))

        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API Collection \"(.*)\" does not has item \"(.*)\"\$")
    void verifyJSONCollectionNotHasItem(String sQuery, String sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, everyItem(not(hasItem(sExpectedValue))))

        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API Array \"(.*)\" has item \"(.*)\"\$")
    void verifyJSONArrayHasItem(String sQuery, String sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, hasItem(sExpectedValue))

        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API Array \"(.*)\" does not has item \"(.*)\"\$")
    void verifyJSONArrayNotHasItem(String sQuery, String sExpectedValue) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, not(hasItem(sExpectedValue)))

        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

















}
