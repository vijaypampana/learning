package learning.BDD.Cucumber.Home

import cucumber.api.DataTable
import cucumber.api.java.en.Given

class firstBDD {

    @Given("^I want to open chrome browser\$")
    void openChromeBrowser() {
        println("Inside Open Chrome Browser Step")
    }

    @Given("^I open the google Home page\$")
    void openGoogleHome() {
        println("Inside Google Home Step")
    }

    @Given("^I search for \"(.*)\"\$")
    void searchText(String val) {
        println("Inside Search Text Step : $val")
    }

    @Given("^I read the data table values\$")
    void readTablevalues(DataTable table) {
        List<List<String>> rows = table.raw()
        rows.each { List<String> row ->
            row.each { val1 -> println (val1)}
        }
    }

    @Given("^I read the map value\$")
    void readMapValue(Map<String, String> map1) {
        println("Inside read Map value Step")
        map1.each { k, v ->
            println("Key value is $k and value is $v")
        }
    }

    @Given("^I read data table and saved it to list of map\$")
    void tabletoMap(DataTable table) {

        List<Map<String, String>> map1 = table.asMaps(String.class, String.class)
        map1.each { val ->
            val.each { k, v ->
                println("Key value is $k and value is $v")
            }
        }
    }

    @Given("^BDD search results are shown\$")
    void results() {
        println("Inside the Search results step")
    }


}
