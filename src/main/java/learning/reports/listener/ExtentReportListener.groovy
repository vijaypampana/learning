package learning.reports.listener

import com.aventstack.extentreports.ExtentReports
import com.aventstack.extentreports.ExtentTest
import com.aventstack.extentreports.markuputils.ExtentColor
import com.aventstack.extentreports.markuputils.MarkupHelper
import com.aventstack.extentreports.reporter.ExtentHtmlReporter
import com.aventstack.extentreports.reporter.configuration.Theme
import org.openqa.selenium.WebDriver

class ExtentReportListener {

    static ExtentReports extent = null
    static ExtentHtmlReporter report = null
    static ExtentTest test = null

    static ExtentReports setup() {

        String reportLocation = "./Reports/Extent_Report.html"
        report = new ExtentHtmlReporter(reportLocation)
        report.config().setDocumentTitle("Automation Test Report")
        report.config().setReportName("Automation Test Report")
        report.config().setTheme(Theme.STANDARD)
        println("Extent Report Location Initialized")
        report.start()

        extent = new ExtentReports()
        extent.attachReporter(report)
        extent.setSystemInfo("UserID", System.getProperty("user.name"))

        return extent

    }


    static void testStepHandle(String testStatus, WebDriver driver, ExtentTest extentTest, Throwable throwable) {

        switch (testStatus) {

            case "FAIL":
                extentTest.fail(MarkupHelper.createLabel("Test Case is Failed : ", ExtentColor.RED))
                extentTest.error(throwable.fillInStackTrace())
                if(driver!=null) {
                    driver.quit()
                }
                break
            case "PASS":
                extentTest.pass(MarkupHelper.createLabel("Test Case is Passed : ", ExtentColor.GREEN))
                break
            default:
                break
        }


    }

}
