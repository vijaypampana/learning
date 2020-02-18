package learning.BDD.utilities.reports

import cucumber.api.Result
import gherkin.pickles.PickleTable

class ReportDriver extends ReportFormatter {

    ReportDriver(String typeOfReport) {
        super()
        reportMeta = new ReportMeta()
        reportMeta.setReportName(setLocalReportPath())
        setReportFormatters(ReporterFactory.getFactory(typeOfReport).getFormatters())
    }


    @Override
    void initialize() {

    }

    @Override
    void endExecution() {

    }

    @Override
    void addFeature(String sKeyword, String sName, String sDescription, List<String> tags) {

    }

    @Override
    void startTest(String sKeyword, String sName, String sDescription, List<String> oCategory) {

    }

    @Override
    void endTest(Long duration, Result.Type result, String sErrorMessage) {

    }

    @Override
    void testPass(String sReportText) {

    }

    @Override
    void testFail(String sReportText) {

    }

    @Override
    void testError(String sReportText) {

    }

    @Override
    void testWarning(String sReportText) {

    }

    @Override
    void testSkip(String sReportText) {

    }

    @Override
    void testInfo(String sReportText) {

    }

    @Override
    void testException(String sReportText) {

    }

    @Override
    void testException(Exception e) {

    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, String argument) {

    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, PickleTable oTable) {

    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition) {

    }

    @Override
    void stopStep(Long duration, Result.Type result, String sErrorMessage) {

    }

    @Override
    void stepPass(String sReportText) {

    }

    @Override
    void stepFail(String sReportText) {

    }

    @Override
    void stepError(String sReportText) {

    }

    @Override
    void stepWarning(String sReportText) {

    }

    @Override
    void stepSkip(String sReportText) {

    }

    @Override
    void stepInfo(String sReportText) {

    }

    @Override
    void stepException(String sReportText) {

    }

    @Override
    void stepException(Exception e) {

    }

    @Override
    void stepCode(String sReportText) {

    }

    @Override
    void stepLabel(String sReportText) {

    }

    @Override
    void stepScreenshot() {

    }

    @Override
    void stepTable(PickleTable table) {

    }
}
