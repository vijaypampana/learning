package learning.BDD.utilities.reports

import learning.BDD.utilities.Context
import learning.BDD.utilities.utilEnum.ReportType
import learning.BDD.utilities.utilEnum.WebDriverType

class ReporterFactory {

    private List<ReportFormatter> formatters = new ArrayList<>()

    static ReporterFactory getFactory(String reportType) {
        return new ReporterFactory(reportType)
    }

    private ReporterFactory(String reportType) {
        if(reportType.equals(ReportType.EXTENT)) {
            getExtentFormatter()
        } else if(reportType.equals(ReportType.RALLY)) {
            getRallyDevFormatter()
        } else if(reportType.equals(ReportType.REPORTPORTAL)) {
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

    void getPerfectoFormatter() {
//        if(Context.getInstance().getConfig().getWebDriverType().equals(WebDriverType.PERFECTO) && Context.getInstance().getWebDriver() != null) {
//            formatters.add(new PerfectoFormatter())
//        }
    }

}
