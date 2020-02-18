package learning.BDD.utilities.reports

import com.aventstack.extentreports.markuputils.Markup
import com.aventstack.extentreports.markuputils.MarkupHelper
import gherkin.pickles.PickleTable

import javax.swing.plaf.basic.BasicToolBarUI

class ReportMeta {

    private String  reportName
    private String  reportTitle
    private String  currentFeatureFile
    private List<String> featureTags
    private Boolean isNewFeatureFile
    private Boolean bTestFailure
    private Boolean bTestSkipped
    private Boolean bStepFailure
    private byte[] screenshot
    private Integer currentStepNumber

    void initializeStep() {
        setCurrentStepNumber(0)
    }

    void incrementStepNumber() {
        setCurrentStepNumber(getCurrentStepNumber() + 1)
    }

    String getReportName() {
        return reportName
    }

    void setReportName(String reportName) {
        this.reportName = reportName
    }

    String getReportTitle() {
        return reportTitle
    }

    void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle
    }

    String getCurrentFeatureFile() {
        return currentFeatureFile
    }

    void setCurrentFeatureFile(String currentFeatureFile) {
        this.currentFeatureFile = currentFeatureFile
    }

    List<String> getFeatureTags() {
        return featureTags
    }

    void setFeatureTags(List<String> featureTags) {
        this.featureTags = featureTags
    }

    Boolean getIsNewFeatureFile() {
        return isNewFeatureFile
    }

    void setIsNewFeatureFile(Boolean isNewFeatureFile) {
        this.isNewFeatureFile = isNewFeatureFile
    }

    Boolean getbTestFailure() {
        return bTestFailure
    }

    void setbTestFailure(Boolean bTestFailure) {
        this.bTestFailure = bTestFailure
    }

    Boolean getbTestSkipped() {
        return bTestSkipped
    }

    void setbTestSkipped(Boolean bTestSkipped) {
        this.bTestSkipped = bTestSkipped
    }

    Boolean getbStepFailure() {
        return bStepFailure
    }

    void setbStepFailure(Boolean bStepFailure) {
        this.bStepFailure = bStepFailure
    }

    byte[] getScreenshot() {
        return screenshot
    }

    void setScreenshot(byte[] screenshot) {
        this.screenshot = screenshot
    }

    Integer getCurrentStepNumber() {
        return currentStepNumber
    }

    void setCurrentStepNumber(Integer currentStepNumber) {
        this.currentStepNumber = currentStepNumber
    }

    Markup getMarkup(PickleTable oTable) {

        Integer iRow=0, iColumn = 0
        String[][] markupTable = null
        oTable.getRows().each { row ->
            row.getCells().each { cell ->
                if(markupTable == null) {
                    markupTable = new String[oTable.getRows().size()][row.getCells().size()]
                }
                markupTable[iRow, iColumn] = cell.getValue()
                iColumn++
            }
            iRow++
            iColumn = 0
        }

        return MarkupHelper.createTable(markupTable)
    }

    //Very Specific to Report Portal TBD
    String buildMultiLineArgument(PickleTable oTable) {

        StringBuilder line1 = new StringBuilder(), line2 = new StringBuilder(), margin = new StringBuilder()
        String docString = ""
        Boolean isBeginning = true
        line1.append("\r\n\r\n")
        line2.append("\r\n")
        margin.append("\r\n")
        Integer iColumn = 1
        String TABLE_SEPERATOR = "|"
        String DOCSTRING_DECORATOR = "\n\"\"\"\n"
        String REPORTPORTALPROPERTIESFILELOCATION = 'reportportal.properties'

        oTable.getRows().each { row ->
            if(isBeginning) {
                line1.append(TABLE_SEPERATOR)
                line2.append(TABLE_SEPERATOR)
            }
            margin.append(TABLE_SEPERATOR)
            row.getCells().each { cell ->
                if(isBeginning) {
                    line1.append("Column " + iColumn).append(TABLE_SEPERATOR)
                    line2.append("-").append(TABLE_SEPERATOR)
                }
                String cellValue = cell.getValue().isEmpty() ? " " : cell.getValue()
                margin.append(" ").append(cellValue).append(" ").append(TABLE_SEPERATOR)
                iColumn++
            }
            margin.append("\r\n")
            isBeginning = false
        }

        if(!docString.isEmpty()) {
            margin.append(DOCSTRING_DECORATOR).append(docString).append(DOCSTRING_DECORATOR)
        }
        return line1.append(line2).append(margin).toString()
    }

}
