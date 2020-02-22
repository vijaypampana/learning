package learning.BDD.utilities.reports

import com.aventstack.extentreports.ExtentReports
import com.aventstack.extentreports.ExtentTest
import com.aventstack.extentreports.GherkinKeyword
import com.aventstack.extentreports.markuputils.ExtentColor
import com.aventstack.extentreports.markuputils.MarkupHelper
import com.aventstack.extentreports.reporter.ExtentHtmlReporter
import cucumber.api.Result
import gherkin.pickles.PickleTable
import learning.BDD.utilities.CommonConfig
import learning.BDD.utilities.Context
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot

class ExtentFormatter extends ReportFormatter {

    private static ExtentReports extentReports
    private ExtentHtmlReporter htmlReporter
    private ExtentTest extentFeature
    private ExtentTest extentScenario
    private ExtentTest extentStep

    @Override
    void initialize() {

        extentReports = new ExtentReports()
        setSystemInfo("user", System.getProperty("user.name"))

        //Common Config
        CommonConfig oConfig = context.getConfig()
        setSystemInfo("webDriverType", oConfig.getWebDriverType().name())

        //Creating extent Report file
        File oFile = new File(context.getReports().getReportMeta().getReportName())

        if(!oFile.exists()) {
            oFile.getParentFile().mkdirs()
        }

        htmlReporter = new ExtentHtmlReporter(oFile)
        htmlReporter.config().setDocumentTitle(Context.getInstance().getReports().getReportMeta().getReportTitle())
        htmlReporter.config().setReportName(Context.getInstance().getReports().getReportMeta().getReportTitle())
        htmlReporter.config().setChartVisibilityOnOpen(false)

        extentReports.attachReporter(htmlReporter)
    }

    void setSystemInfo(String sKey, String sValue) {
        if (StringUtils.isNotEmpty(sValue)) {
            extentReports.setSystemInfo(sKey, sValue)
        }
    }

    @Override
    void endExecution() {

    }

    @Override
    void addFeature(String sKeyword, String sName, String sDescription, List<String> tags) {
        try {
            extentFeature = extentReports.createTest(new GherkinKeyword(sKeyword), StringUtils.isEmpty(sName) ? sKeyword : sName)
        } catch (ClassNotFoundException e) {
            e.printStackTrace()
        }
    }

    @Override
    void startTest(String sKeyword, String sName, String sDescription, List<String> oCategory) {
        sKeyword = sKeyword.equalsIgnoreCase("Scenario Outline") ? "Scenario" : sKeyword
        try {
            extentScenario = extentFeature.createNode(new GherkinKeyword(sKeyword), StringUtils.isEmpty(sName) ? sKeyword : sName )
        } catch (ClassNotFoundException e) {
            e.printStackTrace()
        }

        oCategory.each { category ->
            extentScenario.assignCategory(category)
        }
    }

    @Override
    void endTest(Long duration, Result.Type result, String sErrorMessage) {
        extentReports.flush()
    }

    @Override
    void testPass(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testFail(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testError(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testWarning(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testSkip(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testInfo(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testException(String sReportText) {
        //Not applicable for here
    }

    @Override
    void testException(Exception e) {
        //Not applicable for here
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, String argument) {
        startStep(stepNumber, sKeyword, sStepDefinition)
        stepInfo(argument)
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, PickleTable oTable) {
        startStep(stepNumber, sKeyword, sStepDefinition)
        stepTable(oTable)
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition) {
        try {
            extentStep = extentScenario.createNode(new GherkinKeyword(sKeyword), StringUtils.isEmpty(sStepDefinition) ? sKeyword : sStepDefinition)
        } catch (ClassNotFoundException e) {
            e.printStackTrace()
        }
    }

    @Override
    void stopStep(Long duration, Result.Type result, String sErrorMessage) {
        switch (result) {
            case Result.Type.PASSED:
                if(!context.getReports().getReportMeta().getbStepFailure()) {
                    stepPass(result.name())
                }
                break
            case Result.Type.FAILED:
                stepFail(sErrorMessage)
                break
            case Result.Type.SKIPPED:
                stepSkip(result.name())
                break
            case Result.Type.PENDING:
            case Result.Type.AMBIGUOUS:
            case Result.Type.UNDEFINED:
                stepError(StringUtils.isEmpty(sErrorMessage) ? result.name() : sErrorMessage)
                break
        }
    }

    @Override
    void stepPass(String sReportText) {
        extentStep.pass(sReportText)
    }

    @Override
    void stepFail(String sReportText) {
        extentStep.fail(sReportText)
        stepScreenshot()
    }

    @Override
    void stepError(String sReportText) {
        extentStep.fatal(sReportText)
    }

    @Override
    void stepWarning(String sReportText) {
        extentStep.warning(sReportText)
    }

    @Override
    void stepSkip(String sReportText) {
        extentStep.skip(sReportText)
    }

    @Override
    void stepInfo(String sReportText) {
        extentStep.info(sReportText)
    }

    @Override
    void stepException(String sReportText) {
        extentStep.fatal(sReportText)
        stepScreenshot()
    }

    @Override
    void stepException(Exception e) {
        extentStep.fatal(e)
        stepScreenshot()
    }

    @Override
    void stepCode(String sReportText) {
        extentStep.info(MarkupHelper.createCodeBlock(sReportText))
    }

    @Override
    void stepLabel(String sReportText) {
        extentStep.info(MarkupHelper.createLabel(sReportText, ExtentColor.BLUE))
    }

    @Override
    void stepScreenshot() {
        //if(!context.isAPI())
        try {
            byte[] content = ((TakesScreenshot) context.getWebDriver()).getScreenshotAs(OutputType.BYTES)
            context.getReports().getReportMeta().setScreenshot(content)
            extentStep.addScreenCaptureFromPath("data:image/gif;base64," + Base64.getEncoder().encodeToString(content))
        } catch (IOException e) {
            extentStep.fatal(e.getStackTrace().toString())
        }
    }

    @Override
    void stepTable(PickleTable table) {
        extentStep.info(context.getReports().getReportMeta().getMarkup(table))
    }
}
