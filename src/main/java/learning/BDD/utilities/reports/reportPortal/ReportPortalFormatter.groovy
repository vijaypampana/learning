package learning.BDD.utilities.reports.reportPortal

import com.epam.reportportal.listeners.ListenerParameters
import com.epam.reportportal.service.Launch
import com.epam.reportportal.service.ReportPortal
import com.epam.reportportal.utils.MimeTypeDetector
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ
import com.epam.ta.reportportal.ws.model.StartTestItemRQ
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ
import cucumber.api.Result
import gherkin.pickles.PickleTable
import io.reactivex.Maybe
import learning.BDD.utilities.Context
import learning.BDD.utilities.reports.ReportFormatter
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import rp.com.google.common.base.Supplier
import rp.com.google.common.base.Suppliers
import rp.com.google.common.io.Files

import java.util.function.BiConsumer
import java.util.function.Predicate
import java.util.stream.Collectors

class ReportPortalFormatter extends ReportFormatter {

    ReportPortalDataHolder reportPortalDataHolder
    Context context
    protected Supplier<Launch> RP

    ReportPortalFormatter() {
        if(context == null) {
            context = Context.getInstance()
        }
    }

    @Override
    void initialize() {

    }

    @Override
    void endExecution() {
        afterFeature()
        afterLaunch()
    }

    @Override
    void addFeature(String sKeyword, String sName, String sDescription, List<String> tags) {
        startLaunch()
        if(context.getReports().getReportMeta().isNewFeatureFile()) {
            afterFeature()
        }
        reportPortalDataHolder = new ReportPortalDataHolder()
        reportPortalDataHolder.setCurrentFeatureUri(context.getReports().getReportMeta().getCurrentFeatureFile())
        startFeature(sKeyword, sName, sDescription, tags)
    }

    //This method to start the reportal launch Object
    void startLaunch() {
        if(RP == null) {
            RP = Suppliers.memoize(new Supplier<Launch>() {

                final Date startTime = Calendar.getInstance().getTime()
                @Override
                Launch get() {
                    final ReportPortal reportPortal = ReportPortal.builder().build()
                    ListenerParameters parameters = reportPortal.getParameters()

                    StartLaunchRQ rq = new StartLaunchRQ()
                    rq.setName(parameters.getLaunchName())
                    rq.setStartTime(startTime)
                    rq.setMode(parameters.getLaunchRunningMode())
                    rq.setTags(parameters.getTags())
                    rq.setDescription(parameters.getDescription())

                    Launch launch = reportPortal.newLaunch(rq)
                    return launch
                }
            })
        }
    }

    void afterLaunch() {
        if(RP != null) {
            FinishExecutionRQ finishExecutionRQ = new FinishExecutionRQ()
            finishExecutionRQ.setEndTime(Calendar.getInstance().getTime())
            RP.get().finish(finishExecutionRQ)
        }
    }

    void startFeature(String sKeyword, String sName, String sDescription, List<String> tags) {
        StartTestItemRQ rq = new StartTestItemRQ()
        Maybe<String> root = null   //TBD
        rq.setName(ReportPortalUtils.buildNodeName(sKeyword, ": ", sName, ""))
        rq.setDescription(sDescription)
        rq.setTags(new HashSet<>(tags))
        rq.setStartTime(Calendar.getInstance().getTime())
        rq.setType("SUITE")
        if(root == null) {
            reportPortalDataHolder.setCurrentFeatureId(RP.get().startTestItem(rq))
        } else {
            reportPortalDataHolder.setCurrentFeatureId(RP.get().startTestItem(root, rq))
        }
    }

    //After Feature File functionality from second feature file to end
    void afterFeature() {
        if(reportPortalDataHolder!=null && reportPortalDataHolder.getCurrentFeatureId() != null) {
            ReportPortalUtils.finishTestItem(RP.get(), reportPortalDataHolder.getCurrentFeatureId())
        }
    }

    @Override
    void startTest(String sKeyword, String sName, String sDescription, List<String> oCategory) {
        reportPortalDataHolder.setTags(oCategory.stream().collect(Collectors.toSet()))
        beforeScenario(sKeyword, sName, sDescription)
    }

    //Start Cucumber Scenario
    void beforeScenario(String sKeyword, String sName, String sDescription) {
        Maybe<String> id = ReportPortalUtils.startNonLeafNode(
                RP.get(),
                reportPortalDataHolder.getCurrentFeatureId(),
                ReportPortalUtils.buildNodeName(sKeyword, ": ", sName, ""),
                sDescription,
                reportPortalDataHolder.getTags(),
                "SCENARIO"
        )
        reportPortalDataHolder.setCurrentScenarioId(id)
    }

    @Override
    void endTest(Long duration, Result.Type result, String sErrorMessage) {
        //Not applicable for Here
    }

    @Override
    void testPass(String sReportText) {
        afterScenario(Result.Type.PASSED, sReportText)
    }

    //Finish Cucumber Scenario
    void afterScenario(Result.Type result, String sReportText) {
        ReportPortalUtils.finishTestItem(RP.get(), reportPortalDataHolder.getCurrentScenarioId(), result.toString(), sReportText)
    }

    @Override
    void testFail(String sReportText) {
        afterScenario(Result.Type.FAILED, sReportText)
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
        afterScenario(Result.Type.SKIPPED, sReportText)
    }

    @Override
    void testInfo(String sReportText) {
        stepPass(sReportText)
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
        reportPortalDataHolder.setAllSubSteps(new ArrayList<>())
        reportPortalDataHolder.setCurrentStep(ReportPortalUtils.buildNodeName(
                reportPortalDataHolder.getMarkDownText(sKeyword),
                sStepDefinition,
                argument,
                ""
        ))
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition, PickleTable oTable) {
        reportPortalDataHolder.setAllSubSteps(new ArrayList<>())
        reportPortalDataHolder.setCurrentStep(ReportPortalUtils.buildNodeName(
                reportPortalDataHolder.getMarkDownText(sKeyword),
                sStepDefinition,
                "",
                context.getReports().getReportMeta().buildMultiLineArgument(oTable)
        ))
    }

    @Override
    void startStep(int stepNumber, String sKeyword, String sStepDefinition) {
        reportPortalDataHolder.setAllSubSteps(new ArrayList<>())
        reportPortalDataHolder.setCurrentStep(ReportPortalUtils.buildNodeName(
                reportPortalDataHolder.getMarkDownText(sKeyword),
                sStepDefinition,
                "",
                ""
        ))
    }

    @Override
    void stopStep(Long duration, Result.Type result, String sErrorMessage) {
        switch (result) {
            case Result.Type.PASSED:
                break
            case Result.Type.FAILED:
                stepFail(StringUtils.isEmpty(sErrorMessage) ? result.name() : sErrorMessage)
                break
            case Result.Type.PENDING:
            case Result.Type.AMBIGUOUS:
            case Result.Type.UNDEFINED:
                stepError(StringUtils.isEmpty(sErrorMessage) ? result.name() : sErrorMessage)
                break
            case Result.Type.SKIPPED:
                stepSkip(result.name())
                break
        }

        StringBuilder message = new StringBuilder(reportPortalDataHolder.getCurrentStep())
        StringBuilder subSteps = new StringBuilder()

        reportPortalDataHolder.getAllSubSteps().each { val ->
            def item = val.entrySet().iterator().next()
            subSteps.append("\n" + markTextBold(item.getKey().name()) + " - " + item.getValue())
        }

        reportPortalResult(result, message.append(subSteps).toString())
    }

    protected void reportPortalResult(Result.Type result, String message) {
        def RPLogLevel = ReportPortalConstants.CUCUMBERLOGGERLEVELMAPPING.get(result)
        reportPortalUtils.sendLog(message, RPLogLevel, isScreenShotRequired(RPLogLevel) ? getScreenShotFile() : null)
    }

    Predicate<ReportPortalConstants.ReportPortalLoggerLevels> isScreenShotRequired = { loggerLevel ->
        java.util.stream.Stream.of(ReportPortalConstants.ReportPortalLoggerLevels.ERROR).anyMatch({ screenShotRequired -> screenShotRequired.equals(loggerLevel) })
    }

    //Just for people to notify the text
    String markTextBold(String sValue) {
        return String.format("**%s**", sValue)
    }

    SaveLogRQ.File getScreenShotFile() {
        if(!context.isAPI()) {
            SaveLogRQ.File screenshot = new SaveLogRQ.File()
            try {
                File file = ((TakesScreenshot) context.getWebDriver()).getScreenshotAs(OutputType.FILE)
                screenshot.setContent(Files.toByteArray(file))
                screenshot.setContentType(MimeTypeDetector.detect(file))
                screenshot.setName(UUID.randomUUID().toString())
            } catch (Exception e) {
                println(e.getMessage())
            }
            return screenshot
        }
    }

    //This method will run putReportStep
    BiConsumer<ReportPortalConstants.ReportPortalLoggerLevels, String> resultStepHolder = { loggerLevel, sReportText -> reportPortalDataHolder.putReportStep(loggerLevel, sReportText) }

    @Override
    void stepPass(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.INFO, sReportText)
    }

    @Override
    void stepFail(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.ERROR, sReportText)
    }

    @Override
    void stepError(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.FATAL, sReportText)
    }

    @Override
    void stepWarning(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.WARN, sReportText)
    }

    @Override
    void stepSkip(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.WARN, sReportText)
    }

    @Override
    void stepInfo(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.DEBUG, sReportText)
    }

    @Override
    void stepException(String sReportText) {
        stepError(sReportText)
    }

    @Override
    void stepException(Exception e) {
        stepError(e.getMessage())
    }

    @Override
    void stepCode(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.INFO, "\n```\n" + sReportText + "\n```")
    }

    @Override
    void stepLabel(String sReportText) {
        resultStepHolder.accept(ReportPortalConstants.ReportPortalLoggerLevels.INFO, "*" + sReportText + "*\n")
    }

    @Override
    void stepScreenshot() {
        ReportPortalUtils.sendLog("Screenshot For Wait Screen", ReportPortalConstants.ReportPortalLoggerLevels.INFO, getScreenShotFile())
    }

    @Override
    void stepTable(PickleTable table) {
        stepPass(context.getReports().getReportMeta().getMarkup(table).getMarkup())
    }

    protected void afterStep(Result.Type result) {
        reportPortalResult(result, reportPortalDataHolder.getCurrentStep())
    }

}
