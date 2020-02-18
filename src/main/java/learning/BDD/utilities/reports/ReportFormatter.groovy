package learning.BDD.utilities.reports

import cucumber.api.Result
import gherkin.pickles.PickleTable
import learning.BDD.utilities.Context

abstract class ReportFormatter {

    Context context

    ReportFormatter() {
        if(context == null)
            context = Context.getInstance()
    }

    abstract void initialize()

    abstract void endExecution()

    abstract void addFeature(String sKeyword, String sName, String sDescription, List<String> tags)

    abstract void startTest(String sKeyword, String sName, String sDescription, List<String> oCategory)

    abstract void endTest(Long duration, Result.Type result, String sErrorMessage)

    abstract void testPass(String sReportText)

    abstract void testFail(String sReportText)

    abstract void testError(String sReportText)

    abstract void testWarning(String sReportText)

    abstract void testSkip(String sReportText)

    abstract void testInfo(String sReportText)

    abstract void testException(String sReportText)

    abstract void testException(Exception e)

    abstract void startStep(int stepNumber, String sKeyword, String sStepDefinition, String argument)

    abstract void startStep(int stepNumber, String sKeyword, String sStepDefinition, PickleTable oTable)

    abstract void startStep(int stepNumber, String sKeyword, String sStepDefinition)

    abstract void stopStep(Long duration, Result.Type result, String sErrorMessage)

    abstract void stepPass(String sReportText)

    abstract void stepFail(String sReportText)

    abstract void stepError(String sReportText)

    abstract void stepWarning(String sReportText)

    abstract void stepSkip(String sReportText)

    abstract void stepInfo(String sReportText)

    abstract void stepException(String sReportText)

    abstract void stepException(Exception e)

    abstract void stepCode(String sReportText)

    abstract void stepLabel(String sReportText)

    abstract void stepScreenshot()

    abstract void stepTable(PickleTable table)

}
