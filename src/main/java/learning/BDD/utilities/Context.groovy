package learning.BDD.utilities

import com.browserstack.local.Local
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.AndroidMobileCapabilityType
import io.appium.java_client.remote.IOSMobileCapabilityType
import io.appium.java_client.remote.MobileCapabilityType
import learning.BDD.utilities.reports.ReportDriver
import learning.BDD.utilities.utilEnum.BrowserType
import learning.BDD.utilities.utilEnum.WebDriverType
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
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

        overrideConfig()

        if(!isAPI() && oWebDriver == null) {
            setWebdriver()
        }
    }

    //This method will read the config.yaml file and initialize the Common Config object
    void setConfig() {
        logger.info("Started reading CommonConfig.yaml file")

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)

        try {
            oConfig = mapper.readValue(new File(getClass().getClassLoader().getResource("CommonConfig.yml").getFile()), CommonConfig.class)

        } catch (IOException e) {
            logger.error(e.getMessage())
        }

    }

    //Not sure how this is being used in the code hence not coding now
    void overrideConfig() {
        //TBC
    }

    void overrideConfig(String swebDriverType, String sApplicationType, String sApplicationEnvironment, String sDeviceName, String sBrowserName) {

        if(oConfig == null) {
            setConfig()
        }

        if(StringUtils.isEmpty(swebDriverType)) {
            logger.info("WebDriverType is empty")
        } else {
            oConfig.setWebDriverType(swebDriverType)
        }

        if(StringUtils.isEmpty(sApplicationType)) {
            logger.info("Application Type is empty")
        } else {
            oConfig.setApplicationType(sApplicationType)
        }

        if(StringUtils.isEmpty(sApplicationEnvironment)) {
            logger.info("Application Environment is empty")
        } else {
            oConfig.setApplicationEnv(sApplicationEnvironment)
        }

        if(StringUtils.isEmpty(sDeviceName)) {
            logger.info("Device Name is empty")
        } else {
            oConfig.getCapability().setDeviceName(swebDriverType)
        }

        if(StringUtils.isEmpty(sBrowserName)) {
            logger.info("Browser Name is empty")
        } else {
            oConfig.getCapability().setBrowserName(sBrowserName)
        }

    }

    void overrideConfig(String swebDriverType, String sApplicationType, String sApplicationEnvironment, String sDeviceName, String sBrowserName, String sVersion) {
        if(StringUtils.isEmpty(sVersion)) {
            logger.info("Version is empty")
        } else {
            oConfig.getCapability().setPlatformVersion(sVersion)
        }

        overrideConfig(
                swebDriverType, sApplicationType, sApplicationEnvironment, sDeviceName, sBrowserName)
    }

    private void setWebDriver() {

        String sDeviceName = oConfig.getCapability().getDeviceName()
        BrowserType sBrowserType = oConfig.getCapability().getBrowserName()

        if(sDeviceName.toLowerCase().contains("iphone") || sDeviceName.toLowerCase().contains("ipad")) {
            bIOS = true
        }

        switch (oConfig.getWebDriverType()) {

            case WebDriverType.API:
                break
            case WebDriverType.PERFECTO:
                oCapabilities.setCapability("securityToken", oConfig.getWebDriverAuthentication().getPassword())
                oCapabilities.setCapability("deviceSessionId", oConfig.getCapability().getPerfectoSessionID())
                startPerfectoDriver(sBrowserType, sDeviceName)
                break
            case WebDriverType.PERFECTOWEB:
                oCapabilities.setCapability("securityToken", oConfig.getWebDriverAuthentication().getPassword())
                startPerfectoWebDriver(sBrowserType)
                break
            case WebDriverType.APPIUM:
                startAppiumDriver(sBrowserType, sDeviceName)
                break;
            case WebDriverType.ZALENIUM:
                startBrowserStackTunnel("")
                oCapabilities.setCapability("browserstack.localIdentifier", bsLocalIdentifier)
                oCapabilities.setCapability("project", oConfig.getApplicationType().name())
                oCapabilities.setCapability("recordVideo", true)
                if(StringUtils.isNotEmpty(sDeviceName)) {
                    oCapabilities.setCapability("realMobile", true)
                    oCapabilities.setCapability("device", sDeviceName)
                }
                startRemoteWebDriver(sBrowserType)
                break
            case WebDriverType.BROWSERSTACK:
                startBrowserStackTunnel(oConfig.getWebDriverAuthentication().getPassword())
                oCapabilities.setCapability("project", oConfig.getApplicationType().name())
                oCapabilities.setCapability("browserstack.user", oConfig.getWebDriverAuthentication().getUsername())
                oCapabilities.setCapability("browserstack.key", oConfig.getWebDriverAuthentication().getPassword())
                oCapabilities.setCapability("browserstack.local", true)
                oCapabilities.setCapability("browserstack.localIdentifier", bsLocalIdentifier)
                oCapabilities.setCapability("browserstack.appium_version", "1.9.1")
                if(StringUtils.isNotEmpty(sDeviceName)) {
                    oCapabilities.setCapability("realMobile", true)
                    startAppiumDriver(sBrowserType, sDeviceName)
                } else {
                    startRemoteWebDriver(sBrowserType)
                }
                break
            case WebDriverType.SELENIUMLOCAL:
                startLocalDriver(sBrowserType, sDeviceName)
                break
        }

        //TBD : Tracking Selenium Session ID
        ReportPortalUtils.appendDescription("Session ID: '" + ((RemoteWebDriver) oWebDriver).getSessionId().toString() + "'")

        //Turn on Implicit Wait
        turnOnImplicitWait()

        //Explicit Wait
        oWebDriverWait = new WebDriverWait(oWebDriver, oConfig.getWebDriverTimeOut())
        oWebDriverShortWait = new WebDriverWait(oWebDriver, oConfig.getWebDriverShortWait())
        logger.info(oCapabilities.toString())

    }

    private void startAppiumDriver(BrowserType oBrowserType, String sDeviceName) {
        if(oBrowserType == null) {
            oCapabilities.setBrowserName("")

            if(!oConfig.getCapability().getApp().isEmpty()) {
                //TBD
                oConfig.setCapability(MobileCapabilityType.APP, Utility.getTextByOS(oConfig.getCapability().getApp()))

                if(oConfig.getCapability(MobileCapabilityType.APP).toString().toLowerCase().endsWith(".ipa")) {
                    bIOS = true
                }
            }

            if(bIOS) {
                oCapabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, oConfig.getApplicationType().getsIOSBundleID())
                oCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest")
            } else {
                oCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, oConfig.getApplicationType().getsAndroidPackage())
                oCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, oConfig.getApplicationType().getsAndroidActivity())
                if(!oConfig.getWebDriverType().equals(WebDriverType.PERFECTO)) {
                    oCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2")
                }
            }
        } else {
            switch (oBrowserType) {
                case BrowserType.Firefox:
                case BrowserType.Opera:
                case BrowserType.IE:
                case BrowserType.Edge:
                    break
                case BrowserType.Android:
                    oCapabilities.setBrowserName("Browser")
                    break
                case BrowserType.IOS:
                case BrowserType.Safari:
                    oCapabilities.setBrowserName("Safari")
                    break
                case BrowserType.Chrome:
                    oCapabilities.setBrowserName("Chrome")
                    break
                default:
                    break
            }
        }

        oCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Utility.getTextByOS(sDeviceName))

        if(!oConfig.getCapability().getPlatformName().isEmpty()) {
            oCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, oConfig.getCapability().getPlatformName())
        }

        if(!oConfig.getCapability().getPlatformVersion().isEmpty()) {
            oCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, oConfig.getCapability().getPlatformVersion())
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create()
        logger.info ("Desired Capabilities\n" + gson.toJson(oCapabilities.toJson()))

        if(bIOS) {
            ReportPortalUtils.appendDescription("Platform: 'IOS'")
            ReportPortalUtils.appendTags("iOS")
            oWebDriver = new IOSDriver(oConfig.getWebDriverType().getURL(), oCapabilities)
        } else {
            ReportPortalUtils.appendDescription("Platform: 'Android'")
            ReportPortalUtils.appendTags("Android")
            oWebDriver = new AndroidDriver(oConfig.getWebDriverType().getURL(), oCapabilities)
        }

    }

    private void startPerfectoDriver(BrowserType sBrowserType, String sDeviceName) {

    }

    private void startRemoteWebDriver(BrowserType sBrowserType, String sDeviceName) {

    }

    private void startPerfectoWebDriver(BrowserType sBrowserType, String sDeviceName) {

    }

    private void startLocalDriver(BrowserType oBrowserType, String sDeviceName) {

    }








}
