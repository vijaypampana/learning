package learning.BDD.utilities.utilEnum

enum ReportType {

    PERFECTO("Perfecto"),
    EXTENT("Extent"),
    RALLY("Rally"),
    REPORTPORTAL("ReportPortal"),
    ALL("")

    ReportType(String type) {
        this.type = type
    }
    private final String type;

    String getType() {
        return type
    }
}