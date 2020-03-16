package learning.BDD.utilities.api

import com.fasterxml.jackson.databind.node.JsonNodeType
import cucumber.api.DataTable
import cucumber.api.java.en.Given
import learning.BDD.utilities.Context
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.testng.Assert
import org.testng.annotations.Optional

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
                verifyJsonObjectEqualto(row.get(0).toString(), row.get(1).toString())
            }
        }
    }

    public void verifyJsonObjectEqualto(String sQuery, String sExpectedValue, String type) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            sExpectedValue = Context.getInstance().getData(sExpectedValue)
            Context.getInstance().getValidatableResponse().body(sQuery, equalTo(getJsonType(sExpectedValue, type)))
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API JSON Object \"(.*)\" equal to \"(.*)\"\$")
    public void verifyJsonObjectEqualto(String sQuery, String sExpectedValue) {
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

    @Given("^I verify API JSON Array \"(.*)\" is sorted (ascending|descending)\$")
    void verifyJsonArraySorting(String sQuery, String sortingOrder) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            List<Object> actualArray = getJsonArray(sQuery)
            if ((actualArray.size() > 0) && (!(actualArray.get(0) instanceof HashMap))) {
                Assert.assertTrue(validateJsonArray((ArrayList) actualArray), sortingOrder, "")
            }
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I verify API JSON Array \"(.*)\" contains only \"(.*)\"\$")
    void verifyJsonArrayUnique(String sQuery, String sUnique) {
        try {
            sQuery = Context.getInstance().getData(sQuery)
            List<Object> actualArray = getJsonArray(sQuery)
            if(actualArray.size()>0) {
                Assert.assertTrue(validateJsonArray((ArrayList) actualArray), "checkUnique", sUnique)
            }
        } catch (AssertionError e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
    }

    @Given("^I store API Json Object \"(.*)\" to \"(.*)\"\$")
    void storeJsonObject(String sQuery, String sMapKey) {
        sQuery = Context.getInstance().getData(sQuery)
        String sMapValue = Context.getInstance().getValidatableResponse().extract().body().jsonPath().get(sQuery).toString()
        Context.getInstance().addData(sMapKey, sMapValue)
    }

    static JsonNodeType getJsonNodeType(String sValue) {
        if(sValue == null || sValue.equalsIgnoreCase("NULL")) {
            return JsonNodeType.NULL
        } else if ((sValue.startsWith("\"") && sValue.endsWith("\"")) || (sValue.startsWith("\'") && sValue.endsWith("\'"))) {
            return JsonNodeType.STRING
        } else if ((BooleanUtils.toBooleanObject(sValue) == Boolean.TRUE) || (BooleanUtils.toBooleanObject(sValue) == Boolean.FALSE)) {
            return JsonNodeType.BOOLEAN
        } else if (StringUtils.isNumeric(sValue)) {
            return JsonNodeType.NUMBER
        } else {
            return JsonNodeType.STRING
        }
    }

    //This method will return any data based on Gpath. If query did not match it will return empty array
    static <T> List<T> getJsonArray(String sQuery) {
        sQuery = Context.getInstance().getData(sQuery)
        return Context.getInstance().getResponse().jsonPath().getList(sQuery)
    }

    //This method will act on String Array List
    boolean validateJsonArray(ArrayList<String> list, String sOperation, @Optional String sValue) {
        boolean result = false
        ArrayList<String> actualList;
        switch (sOperation) {
            case ("ascending"):
                actualList = list
                Collections.sort(actualList)
                result = actualList.equals(list)
                break
            case ("descending"):
                actualList = list
                Collections.sort(actualList)
                Collections.reverse(actualList)
                result = actualList.equals(list)
                break
            case ("checkUnique"):
                actualList = list
                actualList.removeAll(sValue)
                result = actualList.isEmpty()
                break
        }
        return result
    }

    static Object getJsonType(String value, String type) {
        switch (type.toUpperCase()) {
            case "INT":
            case "INTEGER":
                return Integer.parseInt(value)
            case "LONG":
                return Long.parseLong(value)
            case "FLOAT":
                return Float.parseFloat(value)
            case "DOUBLE":
                return Double.parseDouble(value)
            case "BOOLEAN":
                return Boolean.parseBoolean(value)
            case "NULL":
                return null
            default:
                return value
        }
    }
}
