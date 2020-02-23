package learning.BDD.utilities.reports.reportPortal

import com.epam.reportportal.listeners.Statuses
import cucumber.api.Result
import rp.com.google.common.collect.ImmutableMap        //com.google.common.collect.ImmutableMap

class ReportPortalConstants {

    public static final String MARKDOWNTEXT = "!!!MARKDOWN_MODE!!!\n"

    public enum ReportPortalLoggerLevels {
        FATAL("FATAL"),
        ERROR("ERROR"),
        WARN("WARN"),
        INFO("INFO"),
        DEBUG("DEBUG"),
        TRACE("TRACE")

        String logLevel
        ReportPortalLoggerLevels(String logLevel) {
            this.logLevel = logLevel
        }

        String getLogLevel() {
            return logLevel
        }

        void setLogLevel(String logLevel) {
            this.logLevel = logLevel
        }
    }

    private static final Map<String, String> STATUS_MAPPING = ImmutableMap.builder()
            .put("passed", Statuses.PASSED)
            .put("skipped", Statuses.SKIPPED)
            .put("undefined", Statuses.SKIPPED).build()

    public static final Map<Result.Type, ReportPortalLoggerLevels> CUCUMBERLOGGERLEVELMAPPING = Collections.unmodifiableMap(
            new EnumMap<Result.Type, ReportPortalLoggerLevels>(Result.Type.class) {{
                put(Result.Type.PASSED, ReportPortalLoggerLevels.INFO)
                put(Result.Type.SKIPPED, ReportPortalLoggerLevels.WARN)
                put(Result.Type.FAILED, ReportPortalLoggerLevels.ERROR)
                put(Result.Type.PENDING, ReportPortalLoggerLevels.FATAL)
                put(Result.Type.UNDEFINED, ReportPortalLoggerLevels.FATAL)
                put(Result.Type.AMBIGUOUS, ReportPortalLoggerLevels.FATAL)
            }}
    )


}
