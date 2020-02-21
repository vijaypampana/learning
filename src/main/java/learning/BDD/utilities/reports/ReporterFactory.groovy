package learning.BDD.utilities.reports

import learning.BDD.utilities.utilEnum.ReportType

class ReporterFactory {

    private List<ReportFormatter> formatters = new ArrayList<>()

    static ReporterFactory getFactory(String reportType) {
        return new ReporterFactory(reportType)
    }

    private ReporterFactory(String reportType) {
        if(reportType.equalsIgnoreCase(ReportType.EXTENT)) {
            getExtentFormatter()
        } else if(reportType.equalsIgnoreCase(ReportType.RALLY)) {
            getRallyDevFormatter()
        } else if(reportType.equalsIgnoreCase(ReportType.REPORTPORTAL)) {
            getReportPortalFormatter()
        } else {
            getExtentFormatter()
        }
    }

    List<ReportFormatter> getFormatters() {
        return formatters
    }

    void setFormatters(List<ReportFormatter> formatters) {
        this.formatters = formatters
    }

    void getExtentFormatter() {
        formatters.add(new ExtentFormatter())
    }

    void getRallyDevFormatter() {
        //if (Context.getInstance().getConfig().getRallyDev() != null)
        //      formatters.add(new RallyDevFormatter())
    }

    void getReportPortalFormatter() {
        //formatter.add(new ReportPortalFormatter())
    }

}