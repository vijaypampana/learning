package learning.BDD.utilities.reports.reportPortal

import io.reactivex.Maybe

class ReportPortalDataHolder {

    private List<String> allSteps
    private List<Map<ReportPortalConstants.ReportPortalLoggerLevels, String>> allSubSteps
    private String currentFeatureUri, currentStep
    private Maybe<String> currentFeatureId, currentScenarioId, currentStepId
    private Set<String> tags

    ReportPortalDataHolder() {
        allSteps = new ArrayList<>()
        allSubSteps = new ArrayList<>()
    }

    public String getMarkDownText(String appendStep) {
        return ReportPortalConstants.MARKDOWNTEXT + appendStep
    }

    public String addStep(String step) {
        allSteps.add(step)
    }

    public void putReportStep(ReportPortalConstants.ReportPortalLoggerLevels loglevel, String sReportText) {
        Map<ReportPortalConstants.ReportPortalLoggerLevels, String> subStep = new HashMap<>()
        subStep.put(loglevel, sReportText)
        allSubSteps.add(subStep)
    }

    List<String> getAllSteps() {
        return allSteps
    }

    void setAllSteps(List<String> allSteps) {
        this.allSteps = allSteps
    }

    List<Map<ReportPortalConstants.ReportPortalLoggerLevels, String>> getAllSubSteps() {
        return allSubSteps
    }

    void setAllSubSteps(List<Map<ReportPortalConstants.ReportPortalLoggerLevels, String>> allSubSteps) {
        this.allSubSteps = allSubSteps
    }

    String getCurrentFeatureUri() {
        return currentFeatureUri
    }

    void setCurrentFeatureUri(String currentFeatureUri) {
        this.currentFeatureUri = currentFeatureUri
    }

    String getCurrentStep() {
        return currentStep
    }

    void setCurrentStep(String currentStep) {
        this.currentStep = currentStep
    }

    Maybe<String> getCurrentFeatureId() {
        return currentFeatureId
    }

    void setCurrentFeatureId(Maybe<String> currentFeatureId) {
        this.currentFeatureId = currentFeatureId
    }

    Maybe<String> getCurrentScenarioId() {
        return currentScenarioId
    }

    void setCurrentScenarioId(Maybe<String> currentScenarioId) {
        this.currentScenarioId = currentScenarioId
    }

    Maybe<String> getCurrentStepId() {
        return currentStepId
    }

    void setCurrentStepId(Maybe<String> currentStepId) {
        this.currentStepId = currentStepId
    }

    Set<String> getTags() {
        return tags
    }

    void setTags(Set<String> tags) {
        this.tags = tags
    }
}
