import cucumber.api.CucumberOptions
import cucumber.api.testng.AbstractTestNGCucumberTests
import learning.BDD.utilities.Context
import learning.BDD.utilities.utilEnum.ReportType
import org.testng.annotations.BeforeClass
import org.testng.annotations.AfterClass

@CucumberOptions(
        features = "src/test/resources/feature",
        tags = ["@Weather_API"],
        //plugin = ["pretty", "html:target/cucumber-html-report", "json:target/cucumber.json",
        plugin = ["cucumber.runtime.formatter.CustomFormatter:Extent"],
        glue = ["learning.BDD.utilities.api"],
        dryRun = false
)
class DevRunner extends AbstractTestNGCucumberTests{

    @BeforeClass(alwaysRun = true)
    void setup() {
        Context.getInstance().overrideConfig("API", "MyCigna", "DEV01", "", "")
    }

    @AfterClass(alwaysRun = true)
    void tearDown() {
        println ("Inside Tear Down")
    }

}


//tags = ["@smoke,@regression"]     ["@smoke or @regression"]     //smoke or regression tags
//tags = ["@smoke", "@regression"]  ["@smoke and @regression"]      //smoke and regression tags
//tags = ["@smoke", "~@regression"] //Runs all with Smoke tests which dont have regression tag


