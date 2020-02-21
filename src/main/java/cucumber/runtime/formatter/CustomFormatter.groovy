//TestSourcesModel class is not available outside the package of cucumber.runtime.formatter
package cucumber.runtime.formatter

import cucumber.api.Result
import cucumber.api.TestCase
import cucumber.api.TestStep
import cucumber.api.event.*
import cucumber.api.formatter.Formatter
import gherkin.ast.Feature
import gherkin.ast.ScenarioDefinition
import gherkin.ast.Step
import gherkin.ast.Tag
import gherkin.pickles.Argument
import gherkin.pickles.PickleString
import gherkin.pickles.PickleTable
import learning.BDD.utilities.Context
import learning.BDD.utilities.reports.ReportDriver
import learning.BDD.utilities.reports.TagFilterService
import org.apache.commons.lang3.StringUtils


class CustomFormatter implements Formatter {

    private TestSourcesModel testSourcesModel = new TestSourcesModel()
    private ReportDriver reportDriver
    private Context context
    private String currentFeatureFile
    private Boolean hasBackGround = false
    private TagFilterService tagFilterService
    private List<TestStep> backgroundList = new ArrayList<>()
    private List<Result> backgroundResultList = new ArrayList<>()

    CustomFormatter(String sReportType) {
        context = Context.getInstance()
        reportDriver = context.getReports(sReportType)
    }

    @Override
    void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadEventHandler)
        publisher.registerHandlerFor(TestRunStarted.class, testRunStartedEventHandler)
        publisher.registerHandlerFor(TestRunFinished.class, testRunFinishedEventHandler)
        publisher.registerHandlerFor(TestCaseStarted.class, testCaseStartedEventHandler)
        publisher.registerHandlerFor(TestCaseFinished.class, testCaseFinishedEventHandler)
        publisher.registerHandlerFor(TestStepStarted.class, testStepStartedEventHandler)
        publisher.registerHandlerFor(TestStepFinished.class, testStepFinishedEventHandler)
    }

    private EventHandler<TestSourceRead> testSourceReadEventHandler = new EventHandler<TestSourceRead>() {
        @Override
        void receive(TestSourceRead event) {
            handleTestSourceReadEvent(event)
        }
    }

    private EventHandler<TestRunStarted> testRunStartedEventHandler = new EventHandler<TestRunStarted>() {
        @Override
        void receive(TestRunStarted event) {
            handleTestRunStartedEvent(event)
        }
    }

    private EventHandler<TestRunFinished> testRunFinishedEventHandler = new EventHandler<TestRunFinished>() {
        @Override
        void receive(TestRunFinished event) {
            handleTestRunFinishedEvent(event)
        }
    }

    private EventHandler<TestCaseStarted> testCaseStartedEventHandler = new EventHandler<TestCaseStarted>() {
        @Override
        void receive(TestCaseStarted event) {
            handleTestCaseStartedEvent(event)
        }
    }

    private EventHandler<TestCaseFinished> testCaseFinishedEventHandler = new EventHandler<TestCaseFinished>() {
        @Override
        void receive(TestCaseFinished event) {
            handleTestCaseFinishedEvent(event)
        }
    }

    private EventHandler<TestStepStarted> testStepStartedEventHandler = new EventHandler<TestStepStarted>() {
        @Override
        void receive(TestStepStarted event) {
            handleTestStepStartedEvent(event)
        }
    }

    private EventHandler<TestStepFinished> testStepFinishedEventHandler = new EventHandler<TestStepFinished>() {
        @Override
        void receive(TestStepFinished event) {
            handleTestStepFinishedEvent(event)
        }
    }

    void handleTestSourceReadEvent(TestSourceRead event) {
        testSourcesModel.addTestSourceReadEvent(event.uri, event)
    }

    void handleTestRunStartedEvent(TestRunStarted event) {}

    void handleTestRunFinishedEvent(TestRunFinished event) {
        reportDriver.endExecution()
    }

    void handleTestCaseStartedEvent(TestCaseStarted event) {
        reportDriver.getReportMeta().initializeStep()
        handleStartOfFeature(event.testCase)
        handleBackGround(event.testCase)
        handleScenario(event.testCase)
    }

    void handleStartOfFeature(TestCase testCase) {
        if(StringUtils.isEmpty(currentFeatureFile) || !currentFeatureFile.equalsIgnoreCase(testCase.getUri())) {
            reportDriver.getReportMeta().setIsNewFeatureFile(true)
        }
        currentFeatureFile = testCase.getUri()
        reportDriver.getReportMeta().setCurrentFeatureFile(currentFeatureFile)
        addFeature(testSourcesModel.getFeature(testCase.getUri()))
    }

    void addFeature(Feature feature) {
        //feature.getTags().stream().map(Tag::getName()).collect(Collectors.toList())            //In Java 8 Going to array and retrieving the function values to a array
        List<String> featureTags = feature.getTags().stream().map{Tag tag -> tag.getName()}.toArray()
        reportDriver.getReportMeta().setFeatureTags(featureTags)
        reportDriver.addFeature(feature.getKeyword(), feature.getName(), feature.getDescription(), featureTags)
    }

    void handleBackGround(TestCase testCase) {
        if(testSourcesModel.hasBackground(testCase.getUri(), testCase.getLine())) {
            hasBackGround = true
        }
    }

    void handleScenario(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSourcesModel.getAstNode(testCase.getUri(), testCase.getLine())
        if(astNode != null) {
            ScenarioDefinition scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode)
            List<String> scenarioTags = testCase.getTags().stream().map{Tag tag -> tag.getName()}.toArray()
            List<String> filteredTags = tagFilterService.getFilteredTags(scenarioTags, reportDriver.getReportMeta().getFeatureTags())
            reportDriver.startTest(scenarioDefinition.getKeyword(), scenarioDefinition.getName(), scenarioDefinition.getDescription(), filteredTags)
        }
    }

    void handleTestCaseFinishedEvent(TestCaseFinished event) {
        reportDriver.endTest(event.result.getDuration(), event.result.getStatus(), event.result.getErrorMessage())
    }

    void handleTestStepStartedEvent(TestStepStarted event) {
        if(!event.testStep.isHook()) {
            if(isFirstStepAfterBackground(event.testStep)) {

                backgroundList.each { TestStep testStep ->
                    handleStep(testStep)
                }

                backgroundResultList.each {Result result ->
                    reportDriver.stopStep(result.getDuration(), result.getStatus(), result.getErrorMessage())
                }

                hasBackGround = false
                backgroundList.clear()
                backgroundResultList.clear()
            }

            if(hasBackGround) {
                backgroundList.add(event.testStep)
            } else {
                handleStep(event.testStep)
            }
        }
    }

    Boolean isFirstStepAfterBackground(TestStep testStep) {
        TestSourcesModel.AstNode astNode = testSourcesModel.getAstNode(currentFeatureFile, testStep.getStepLine())
        if(astNode != null) {
            if(!TestSourcesModel.isBackgroundStep(astNode)) {
                return true
            }
        }
        return false
    }

    void handleStep(TestStep testStep) {
        TestSourcesModel.AstNode astNode = testSourcesModel.getAstNode(currentFeatureFile, testStep.getStepLine())
        if(astNode != null) {
            Step step = (Step) astNode.node
            if(testStep.getStepArgument().isEmpty()) {
                reportDriver.startStep(reportDriver.getReportMeta().getCurrentStepNumber(), step.getKeyword(), step.getText())
            } else {
                Argument argument = testStep.getStepArgument().get(0)
                if(argument instanceof PickleString) {
                    reportDriver.startStep(reportDriver.getReportMeta().getCurrentStepNumber(), step.getKeyword(), step.getText(), ((PickleString) argument).getContent())
                } else if (argument instanceof PickleTable) {
                    reportDriver.startStep(reportDriver.getReportMeta().getCurrentStepNumber(), step.getKeyword(), step.getText(), ((PickleTable) argument))
                }
            }
        }
    }

    void handleTestStepFinishedEvent(TestStepFinished event) {
        if(!event.testStep.isHook()) {
            if(hasBackGround) {
                backgroundResultList.add(event.result)
            } else {
                reportDriver.stopStep(event.result.getDuration(),
                        (event.result.getStatus().equals(Result.Type.PASSED) && reportDriver.getReportMeta().bStepFailure()) ? Result.Type.FAILED : event.result.getStatus(),
                        event.result.getErrorMessage())
            }
        }
        reportDriver.getReportMeta().incrementStepNumber()
    }
}
