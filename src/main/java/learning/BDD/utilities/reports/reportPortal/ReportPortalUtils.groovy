package learning.BDD.utilities.reports.reportPortal

import com.epam.reportportal.service.Launch
import com.epam.reportportal.service.ReportPortal
import com.epam.reportportal.utils.properties.ListenerProperty
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ
import com.epam.ta.reportportal.ws.model.StartTestItemRQ
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ
import io.reactivex.Maybe
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import rp.com.google.common.base.Function

import javax.annotation.Nullable

class ReportPortalUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportPortalUtils.class)

    ReportPortalUtils() {

    }

    public static String buildNodeName(String prefix, String infix, String argument, String suffix) {
        return ( prefix == null ? "" : prefix) + infix + argument + ( suffix == null ? "" : suffix)
    }

    public static void finishTestItem(Launch launch, Maybe<String> stringMaybe) {
        finishTestItem(launch, stringMaybe, null, null)
    }

    public static void finishTestItem(Launch launch, Maybe<String> stringMaybe, String status, String sReportText) {
        if(stringMaybe == null) {
            LOGGER.error("BUG: StringMaybe (ItemID) cannot be null")
            return
        }

        FinishTestItemRQ rq = new FinishTestItemRQ()
        rq.setStatus(status)
        rq.setEndTime(Calendar.getInstance().getTime())
        launch.finishTestItem(stringMaybe, rq)
    }

    public static Maybe<String> startNonLeafNode(Launch launch, Maybe<String> rootItemId, String sName, String sDescription, Set<String> tags, String type) {
        StartTestItemRQ rq = new StartTestItemRQ()
        rq.setDescription(sDescription)
        rq.setName(sName)
        rq.setStartTime(Calendar.getInstance().getTime())
        rq.setTags(tags)
        rq.setType(type)

        return launch.startTestItem(rootItemId, rq)
    }

    public static void sendLog(String message, final ReportPortalConstants.ReportPortalLoggerLevels level, final SaveLogRQ.File file) {
        ReportPortal.emitLog(new Function<String, SaveLogRQ>() {
            @Override
            SaveLogRQ apply(@Nullable String item) {
                SaveLogRQ rq = new SaveLogRQ()
                rq.setMessage(message)
                rq.setTestItemId(item)
                rq.setLevel(level.getLogLevel())
                rq.setLogTime(Calendar.getInstance().getTime())
                if(file != null) {
                    rq.setFile(file)
                }
                return rq
            }
        })
    }

    public static void appendDescription(String s) {
        appendProperty(ListenerProperty.DESCRIPTION.getPropertyName(), s, "\n")
    }

   public static void appendTags(String s) {
       appendProperty(ListenerProperty.LAUNCH_TAGS.getPropertyName(), s, ";")
   }

    public static void appendProperty(String sName, String sValue, String sDelimiter) {
        String currentValue = System.getProperty(sName)
        System.setProperty(sName, (currentValue == null ? "" : currentValue + sDelimiter) + sValue)
    }

    /**
     * MAP Cucumber Statuses to RP Log Level
     *
     * @Param cukesStatus - CucumberStatus
     * @return reguler log level
     */
    public static String mapLevel(String cukesStatus) {
        String mapped = null;
        if(cukesStatus.equalsIgnoreCase("passed")) {
            return "INFO"
        } else if (cukesStatus.equalsIgnoreCase("skipped")) {
            return "WARN"
        } else {
            return "ERROR"
        }
    }


}
