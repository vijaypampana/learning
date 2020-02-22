package learning.BDD.utilities.reports

import cucumber.api.Result
import gherkin.pickles.PickleTable
import org.testng.Assert

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportDriver extends ReportFormatter {

    List<ReportFormatter> reportFormatters = new ArrayList<>()
    ReportMeta reportMeta

    ReportDriver(String typeOfReport) {
        super()
        reportMeta = new ReportMeta()
        reportMeta.setReportName(setLocalReportPath())
        setReportFormatters(ReporterFactory.getFactory(typeOfReport).getFormatters())
    }

    String getReportDirectoryName() {
        String sDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MMM_yyyy"))
        return "reports".concat(File.separator).concat("Run_",sDate)
    }

    String setLocalReportPath() {
        String sTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH_mm_ss"))
        return getReportDirectoryName().concat(File.separator).concat(sTime, "__").concat(context.isAPI() ? "API" : "UI" ).concat(".html")
    }

    void setReportFormatters(List<ReportFormatter> reportFormatterList) {
        this.reportFormatters = reportFormatterList
    }

    ReportMeta getReportMeta() {
        return reportMeta
    }

    @Override
    void initialize() {
        reportFormatters.each { formatters ->
            formatters.initialize()
        }
    }

    @Override
    void endExecution() {
        reportFormatters.each { formatters ->
            formatters.endExecution()
        }
    }

    @Override
    void addFeature(String sKeyword, String sName, String sDescription, List<String> tags) {
        reportFormatters.each { formatters ->
            formatters.addFeature(sKeyword, sName, sDescription, tags)
        }
    }

    @Override
    void startTest(String sKeyword, String sName, String sDescription, List<String> oCategory) {
        reportMeta.setbTestFailure(false)
        reportFormatters.each { formatters ->
            formatters.startTest(sKeyword, sName, sDescription, oCategory)
        }
    }

    @Override
    void endTest(Long duration, Result.Type result, String sErrorMessage) {
        switch (result) {
            case Result.Type.PASSED:
                if(reportMeta.getbTestFailure()) {
                    testFail(sErrorMessage)
                } else {
                    testPass(result.name())
                }
                break
            case Result.Type.FAILED:
                testFail(sErrorMessage)
                break
            case Result.Type.SKIPPED:
                testSkip(result.name())
                reportMeta.setbTestSkipped(true)
                break
            case Result.Type.UNDEFINED:
                testError(result.name())
                break
            case Result.Type.PENDING:
            case Result.Type.AMBIGUOUS:
                testError(sErrorMessage)
                break
        }

        reportFormatters.each { formatter ->
            formatter.endTest(duration, result, sErrorMessage)
        }

        if(reportMeta.bTestFailure) {
            Assert.assertTrue(false)   // This line is just to fail the test case
        }
    }

    @Override
    void testPass(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testPass(sReportText)
        }
    }

    @Override
    void testFail(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testFail(sReportText)
        }
    }

    @Override
    void testError(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testError(sReportText)
        }
    }

    @Override
    void testWarning(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testWarning(sReportText)
        }
    }

    @Override
    void testSkip(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testSkip(sReportText)
        }
    }

    @Override
    void testInfo(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testInfo(sReportText)
        }
    }

    @Override
    void testException(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.testException(sReportText)
        }
    }

    @Override
    void testException(Exception e) {
        reportFormatters.each { formatters ->
            testException(e.getMessage())
        }
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, String argument) {
        reportMeta.setbTestFailure(false)
        reportFormatters.each { formatters ->
            formatters.startStep(stepNumber, sKeyword, sStepDefinition, argument)
        }
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, PickleTable oTable) {
        reportMeta.setbTestFailure(false)
        reportFormatters.each { formatters ->
            formatters.startStep(stepNumber, sKeyword, sStepDefinition, oTable)
        }
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition) {
        reportMeta.setbTestFailure(false)
        reportFormatters.each { formatters ->
            formatters.startStep(stepNumber, sKeyword, sStepDefinition)
        }
    }

    @Override
    void stopStep(Long duration, Result.Type result, String sErrorMessage) {
        reportFormatters.each { formatters ->
            formatters.stopStep(duration, result, sErrorMessage)
        }
    }

    @Override
    void stepPass(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.stepPass(sReportText)
        }
    }

    @Override
    void stepFail(String sReportText) {
        reportMeta.setbStepFailure(true)
        reportMeta.setbTestFailure(true)
        reportFormatters.each { formatters ->
            formatters.stepFail(sReportText)
        }
    }

    @Override
    void stepError(String sReportText) {
        reportMeta.setbStepFailure(true)
        reportMeta.setbTestFailure(true)
        reportFormatters.each { formatters ->
            formatters.stepPass(sReportText)
        }
    }

    @Override
    void stepWarning(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.stepWarning(sReportText)
        }
    }

    @Override       //Not applicable at step level
    void stepSkip(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.stepSkip(sReportText)
        }
    }

    @Override
    void stepInfo(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.stepInfo(sReportText)
        }
    }

    @Override
    void stepException(String sReportText) {
        reportMeta.setbStepFailure(true)
        reportMeta.setbTestFailure(true)
        reportFormatters.each { formatters ->
            formatters.stepException(sReportText)
        }
    }

    @Override
    void stepException(Exception e) {
        stepException(e.getMessage())
    }

    @Override
    void stepCode(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.stepCode(sReportText)
        }
    }

    @Override
    void stepLabel(String sReportText) {
        reportFormatters.each { formatters ->
            formatters.stepLabel(sReportText)
        }
    }

    @Override
    void stepScreenshot() {
        reportFormatters.each { formatters ->
            formatters.stepScreenshot()
        }
    }

    @Override
    void stepTable(PickleTable table) {
        reportFormatters.each { formatters ->
            formatters.stepTable(table)
        }
    }
}
