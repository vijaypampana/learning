import cucumber.api.CucumberOptions
import cucumber.api.testng.AbstractTestNGCucumberTests
import learning.BDD.utilities.utilEnum.ReportType
import org.testng.annotations.BeforeClass
import org.testng.annotations.AfterClass

@CucumberOptions(
        features = "src/test/resources/feature",
        tags = ["@smoke or @regression"],
        //plugin = ["pretty", "html:target/cucumber-html-report", "json:target/cucumber.json",
        plugin = ["cucumber.runtime.formatter.CustomFormatter:Extent"],
        glue = ["learning.BDD.utilities.api"],
        dryRun = false
)
class DevRunner extends AbstractTestNGCucumberTests{

    @BeforeClass(alwaysRun = true)
    void setup() {
        println ("Inside Setup")
    }

    @AfterClass(alwaysRun = true)
    void tearDown() {
        println ("Inside Tear Down")
    }

}
