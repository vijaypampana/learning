package learning.BDD.utilities

import com.browserstack.local.Local
import io.appium.java_client.AppiumDriver
import learning.BDD.utilities.reports.ReportDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Context extends APIContext {

    //Thread Safe
    private static ThreadLocal THREAD_LOCAL = new ThreadLocal() {
        @Override
        protected Context initialValue() {
            return new Context()
        }
    }

    public static Context getInstance() {
        return (Context) THREAD_LOCAL.get()
    }

    public static void setContext(final Context context) {
        THREAD_LOCAL.set(context)
    }

    public static void remove() {
        THREAD_LOCAL.remove()
    }

//    private static final org.testng.log4testng.Logger logger = Logger.getLogger(Context.class)
    private static final Logger logger = LoggerFactory.getLogger(Context.class)

    private static final int IMPLICIT_TIMEOUT_IN_SECONDS = 15

    private static final String BASE_PACKAGE = "learning.BDD"

    boolean jenkinFlag = false
    boolean bIOS = false

    //**TBD**
    private ReportDriver reports

    private DesiredCapabilities oCapabilities = new DesiredCapabilities()
    private WebDriver oWebDriver
    private WebDriverWait oWebDriverWait
    private WebDriverWait oWebDriverShortWait
    private CommonConfig oConfig

    private String sCurrentPage = ""
    private Map<String, Object> oPageInstance = new HashMap<>()

    //Browser Stack variables
    Local bsLocal = null
    String bsLocalIdentifier = ""

    DesiredCapabilities getCapabilities() {
        return oCapabilities
    }

    WebDriver getWebDriver() {
        return oWebDriver
    }

    AppiumDriver getAppiumDriver() {
        return oWebDriver
    }

    WebDriverWait getWebDriverWait() {
        return oWebDriverWait
    }

    WebDriverWait getWebDriverShortWait() {
        return oWebDriverShortWait
    }

    CommonConfig getConfig() {
        return oConfig
    }

    boolean getJenkinFlag() {
        return jenkinFlag
    }

    void setJenkinFlag(jenkinFlag) {
        this.jenkinFlag = jenkinFlag
    }

    String getCurrentPage() {
        return sCurrentPage
    }

    void setCurrentPage(sCurrentPage) {
        this.sCurrentPage = sCurrentPage
    }

    Map<String, Object> getoPageInstance() {
        return oPageInstance
    }

    //TBD
    Map<String, Object> getoCurrentDataMap() {
        return DataUtil.get()
    }

    void clearPageInstance() {
        oPageInstance.clear()
    }

    //This method will start browser Stack tunnel
    private void startBrowserStackTunnel(String key) {
        bsLocal = new Local()
        bsLocalIdentifier = learning.BDD.utilities.Utility.router("[ALPHANUMERIC~~15~~Random]")

        try {
            HashMap<String, String> bsLocalArgs = new HashMap<>()
            bsLocalArgs.put("key", key)
            bsLocalArgs.put("force", "true")
            bsLocalArgs.put("onlyAutomate", "true")
            bsLocalArgs.put("forceLocal", "true")
            bsLocalArgs.put("localIdentifier", bsLocalIdentifier)   //Todo Revisit during parallel testing

            bsLocal.start(bsLocalArgs)
            logger.info("Browser Stack Tunnel Status : " + bsLocal.isRunning())

        } catch (Exception e) {
            logger.error("Unable to Start Browser Stack Tunnel : " + e.printStackTrace())
        }

    }

    //This method will stop the browser stack tunnel
    private void stopBSTunnel() {
        try {
            if (bsLocal != null) {
                bsLocal.stop()
            }
        } catch (Exception e) {
            logger.error("Unable to stop Browser Stack Tunnel : " + e.printStackTrace())
        }
    }

    public void startDriver() {
        if(oConfig == null) {
            setConfig()
        }

        if(!isAPI() && oWebDriver == null) {
            setWebdriver()
        }
    }




}
