package learning.BDD.utilities.reports

import com.perfecto.reportium.model.PerfectoExecutionContext
import com.perfecto.reportium.model.Project
import com.perfecto.reportium.test.TestContext
import com.perfecto.reportium.test.result.TestResultFactory
import cucumber.api.Result
import gherkin.pickles.PickleTable
import com.perfecto.reportium.client.*
import learning.BDD.utilities.Context
import org.apache.commons.lang3.StringUtils

class PerfectoFormatter extends ReportFormatter {

    private ReportiumClient reportiumClient
    private Context context

    PerfectoFormatter() {
        context = Context.getInstance()
    }

    @Override
    void initialize() {
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
            .withProject(new Project(context.getConfig().getApplicationType().name(), "1.0"))
            .withContextTags("BDD")
            .withWebDriver(context.getAppiumDriver())
            .build()

        reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext)
    }

    @Override
    void endExecution() {
        //NA from here
    }

    @Override
    void addFeature(String sKeyword, String sName, String sDescription, List<String> tags) {
        //NA from here
    }

    @Override
    void startTest(String sKeyword, String sName, String sDescription, List<String> oCategory) {
        reportiumClient.testStart(sKeyword+" - "+sName, new TestContext.Builder().withTestExecutionTags(oCategory).build())
    }

    @Override
    void endTest(Long duration, Result.Type result, String sErrorMessage) {
        //NA from here
    }

    @Override
    void testPass(String sReportText) {
        reportiumClient.testStop(TestResultFactory.createSuccess())
    }

    @Override
    void testFail(String sReportText) {
        reportiumClient.testStop(TestResultFactory.createFailure(sReportText))
    }

    @Override
    void testError(String sReportText) {
        testFail(sReportText)
    }

    @Override
    void testWarning(String sReportText) {
        testPass(sReportText)
    }

    @Override
    void testSkip(String sReportText) {
        testFail(sReportText)
    }

    @Override
    void testInfo(String sReportText) {
        testPass(sReportText)
    }

    @Override
    void testException(String sReportText) {
        testFail(sReportText)
    }

    @Override
    void testException(Exception e) {
        testFail(e.getMessage())
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
        reportiumClient.stepStart(sKeyword+" "+sStepDefinition)
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
            case Result.Type.SKIPPED:
            case Result.Type.UNDEFINED:
            case Result.Type.PENDING:
            case Result.Type.AMBIGUOUS:
                stepFail(StringUtils.isEmpty(sErrorMessage)? result.name() : sErrorMessage)
                break
        }
    }

    @Override
    void stepPass(String sReportText) {
        reportiumClient.reportiumAssert(sReportText, true)
    }

    @Override
    void stepFail(String sReportText) {
        reportiumClient.reportiumAssert(sReportText, false)
    }

    @Override
    void stepError(String sReportText) {
        stepFail(sReportText)
    }

    @Override
    void stepWarning(String sReportText) {
        stepPass(sReportText)
    }

    @Override
    void stepSkip(String sReportText) {
        //Not Applicable for Here
    }

    @Override
    void stepInfo(String sReportText) {
        stepPass(sReportText)
    }

    @Override
    void stepException(String sReportText) {
        stepFail(sReportText)
    }

    @Override
    void stepException(Exception e) {
        stepFail(e.getMessage())
    }

    @Override
    void stepCode(String sReportText) {
        stepPass(sReportText)
    }

    @Override
    void stepLabel(String sReportText) {
        stepPass(sReportText)
    }

    @Override
    void stepScreenshot() {
        //Not applicable from here
    }

    @Override
    void stepTable(PickleTable table) {
        stepPass(context.getReports().getReportMeta().getMarkup(table).getMarkup())
    }
}
