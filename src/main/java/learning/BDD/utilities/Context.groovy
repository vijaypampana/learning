package learning.BDD.utilities

import com.browserstack.local.Local
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.AndroidMobileCapabilityType
import io.appium.java_client.remote.IOSMobileCapabilityType
import io.appium.java_client.remote.MobileCapabilityType
import learning.BDD.utilities.reports.ReportDriver
import learning.BDD.utilities.util.CoreUtil
import learning.BDD.utilities.util.DataUtil
import learning.BDD.utilities.util.Utility
import learning.BDD.utilities.utilEnum.BrowserType
import learning.BDD.utilities.utilEnum.WebDriverType
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.ie.InternetExplorerOptions
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.TimeUnit

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
        bsLocalIdentifier = Utility.router("[ALPHANUMERIC~~15~~Random]")

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

        if(sBrowserType == null) {
            oCapabilities.setBrowserName("mobileOS")

            if(oConfig.getCapability(MobileCapabilityType.APP).toString().toLowerCase().endsWith(".ipa")) {
                bIOS = true
            }

            if(bIOS) {
                oConfig.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest")
            } else {
                oConfig.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2")
            }
        } else {
            switch (sBrowserType) {
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

        if(isMobile()) {
            try {
                Thread.sleep(5000)
            } catch (InterruptedException e) {
                logger.error(e.getMessage())
            }

            if(!oConfig.getCapability().getApp().isEmpty()) {
                PerfectoUtils.installApp(Utility.getTextByOS(oConfig.getCapability().getApp()))
            }

            PerfectoUtils.launchApp(bIOS ? oConfig.getApplicationType().getsIOSBundleID() : oConfig.getApplicationType().getsAndroidPackage())
            getAppiumDriver().context("NATIVE_APP")


        }

    }

    private void startRemoteWebDriver(BrowserType sBrowserType) {
        switch (sBrowserType) {
            case BrowserType.Firefox:
                oCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions())
                oCapabilities.setBrowserName("firefox")
                break
            case BrowserType.Opera:
                oCapabilities.setBrowserName("opera")
                break
            case BrowserType.IE:
                oCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true)
                oCapabilities.setBrowserName("internet explorer")
                break
            case BrowserType.Edge:
                oCapabilities.setBrowserName("MicrosoftEdge")
                break
            case BrowserType.Android:
                oCapabilities.setBrowserName("android")  //To do
                break
            case BrowserType.IOS:
                oCapabilities.setBrowserName("iPhone")  //To do
                break
            case BrowserType.Safari:
                oCapabilities.setBrowserName("safari")
                break
            case BrowserType.Chrome:
                oCapabilities.setCapability(ChromeOptions.CAPABILITY, getChromeOptions())
                oCapabilities.setBrowserName("chrome")
                break
            default:
                //To do custom error reporting
                break
        }

        if(!oConfig.getCapability().getPlatformName().isEmpty()) {
            oCapabilities.setCapability(CapabilityType.PLATFORM_NAME, oConfig.getCapability().getPlatformName().toUpperCase())
        }

        if(!oConfig.getCapability().getPlatformVersion().isEmpty()) {
            oCapabilities.setCapability(CapabilityType.VERSION, oConfig.getCapability().getPlatformVersion())
        }

        oWebDriver = new RemoteWebDriver(oConfig.getWebDriverType().getURL(), oCapabilities)

    }

    private void startPerfectoWebDriver(BrowserType sBrowserType) {
        switch (sBrowserType) {
            case BrowserType.Firefox:
                oConfig.getCapability().setPlatformName("Windows")
                oCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions())
                oCapabilities.setBrowserName("firefox")
                break
            case BrowserType.IE:
                oConfig.getCapability().setPlatformName("Windows")
                oCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true)
                oCapabilities.setBrowserName("internet explorer")
                break
            case BrowserType.Edge:
                oConfig.getCapability().setPlatformName("Windows")
                oCapabilities.setBrowserName("MicrosoftEdge")
                break
            case BrowserType.Safari:
                oConfig.getCapability().setPlatformName("Mac")
                oCapabilities.setBrowserName("safari")
                break
            case BrowserType.Chrome:
                oConfig.getCapability().setPlatformName("Windows")
                oCapabilities.setCapability(ChromeOptions.CAPABILITY, getChromeOptions())
                oCapabilities.setBrowserName("chrome")
                break
            default:
                //To do custom error reporting
                break
        }

        oCapabilities.setCapability("resolution", "1920*1080")
        if(!oConfig.getCapability().getPlatformName().isEmpty()) {
            oCapabilities.setCapability(CapabilityType.PLATFORM_NAME, oConfig.getCapability().getPlatformName().toUpperCase())
        }

        if(!oConfig.getCapability().getPlatformVersion().isEmpty()) {
            oCapabilities.setCapability(CapabilityType.VERSION, oConfig.getCapability().getPlatformVersion())
        } else {
            oCapabilities.setCapability(CapabilityType.VERSION. "latest")
        }

        oWebDriver = new RemoteWebDriver(oConfig.getWebDriverType().getURL(), oCapabilities)
    }

    private void startLocalDriver(BrowserType oBrowserType, String sDeviceName) {
        switch (oBrowserType) {
            case BrowserType.Firefox:
                oWebDriver = new FirefoxDriver(getFirefoxOptions())
                break
            case BrowserType.Opera:
                oWebDriver = new OperaDriver()
                break
            case BrowserType.IE:
                oWebDriver = new InternetExplorerDriver(getInternetExplorerOptions())
                break
            case BrowserType.Edge:
                oWebDriver = new EdgeDriver()
                break
            case BrowserType.Android:
                //To do
                break
            case BrowserType.IOS:
                //To do
                break
            case BrowserType.Safari:
                oWebDriver = new SafariDriver()
                break
            case BrowserType.Chrome:
                oWebDriver = new ChromeDriver(getChromeOptions(sDeviceName))
                break
            default:
                //To do custom error reporting
                break
        }

        oWebDriver.manage().window().maximize()
    }

    private ChromeOptions getChromeOptions() {
        return getChromeOptions("")
    }

    private ChromeOptions getChromeOptions(String sDeviceName) {
        ChromeOptions options = new ChromeOptions()
        if(StringUtils.isEmpty(sDeviceName)) {
            options.addArguments("disable-extensions")
            options.addArguments("--start-maximized")
            options.addArguments("disable-infobars")
            options.setExperimentalOption("useAutomationExtension", false)
        } else {
            Map<String, String> mobileEmulation = new HashMap<>()
            mobileEmulation.put("deviceName", sDeviceName)
            options.setExperimentalOption("mobileEmulation", mobileEmulation)
        }
        return options
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile()
        profile.setPreference("network.automatic-ntlm-auth.trusted-uris", ".internal.cigna.com,.cigna.com,.cignaglobal.com")
        oCapabilities.setCapability(FirefoxDriver.PROFILE, profile)
        oCapabilities.setCapability("acceptInsecureCerts", true)
        oCapabilities.setCapability("moz:webdriverClick", false)
        return new FirefoxOptions(oCapabilities)
    }

    private InternetExplorerOptions getInternetExplorerOptions() {
        InternetExplorerOptions ieOptions = new InternetExplorerOptions()
        ieOptions.destructivelyEnsureCleanSession()
        return ieOptions
    }

    private void closeDriver() {
        if(oWebDriver!=null) {
            oWebDriver.quit()
            stopBSTunnel()
        }
    }

    private void turnOnImplicitWait() {
        oWebDriver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
    }

    private void turnOffImplicitWait() {
        oWebDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS)
    }

    private boolean isAndroid() {
        boolean bReturn = false
        if(oWebDriver instanceof AndroidDriver && oConfig.getCapability().getBrowserName() == null) {
            bReturn = true
        }
        return bReturn
    }

    private boolean isIOS() {
        boolean bReturn = false
        if(oWebDriver instanceof IOSDriver && oConfig.getCapability().getBrowserName() == null) {
            bReturn = true
        }
        return bReturn
    }

    private boolean isMobile() {
        boolean bReturn = false
        if(isIOS() || isAndroid()) {
            bReturn = true
        }
        return bReturn
    }

    private boolean isAPI() {
        boolean bReturn = false
        if(oConfig.getWebDriverType().equals(WebDriverType.API)) {
            bReturn = true
        }
        return bReturn
    }

    private boolean isWeb() {
        boolean bReturn = false
        if(!isMobile() && !isAPI()) {
            bReturn = true
        }
        return bReturn
    }

    private String getPlatform() {
        if(isMobile())
            return "Mobile"
        else if(isWeb())
            return "Web"
        else if(isAPI())
            return "API"
        else
            return "Unknown"
    }

    private String getMobilePlatform() {
        if(isAndroid()) {
            return "Android"
        } else if(isIOS()) {
            return "IOS"
        } else {
            return "Not Mobile"
        }
    }

    ReportDriver getReports() {
        return reports
    }

    ReportDriver getReports(String type) {
        setReports(type)
        return getReports()
    }

    //TBD
    void setReports(String type) {
        if(reports == null) {
            reports = new ReportDriver(type)
            reports.initialize()
        }
    }

    //Find Element using By
    boolean find(By oBy) {
        return findElement(oBy).isDisplayed()
    }

    //Find Element Using String object
    boolean find(String sObject) {
        return findElement(sObject).isDisplayed()
    }

    //Find WebElement
    boolean find(WebElement element) {
        return element.isDisplayed()
    }

    WebElement findElement(By oBy) {
        return oWebDriver.findElement(oBy)
    }

    WebElement findElement(String sWebElement) {
        return findElement(sCurrentPage, sWebElement)
    }

    WebElement findElement(String sPage, String sWebElement) {
        WebElement element = searchElement(getPageInstance(sPage), sWebElement)
        if(element == null) {
            logger.error("$sWebElement is missing in the pageFactory")
        }
        return element
    }

    Object getPageInstance(String sPage) {
        String sSeparator = "."
        if(!oPageInstance.containsKey(sPage)) {
            try {
                oPageInstance.put(sPage, Class.forName(BASE_PACKAGE + sSeparator + oConfig.getApplicationType().name().toLowerCase() + sSeparator + getPlatform().toLowerCase() + processPage(sPage)).getDeclaredConstructor().newInstance())
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException error) {
                logger.error(error.getMessage())
            }
        }
        return oPageInstance.get(sPage)
    }

    private String processPage(String sPage) {
        String sReturn = ".pagefactory"
        String[] aPage = sPage.split("\\.")
        aPage.each { val ->
            sReturn += "." + val.toLowerCase()
        }
        return sReturn
    }

    MobileElement findMobileElement(By oBy) {
        return (MobileElement) getAppiumDriver().findElement(oBy)
    }

    MobileElement findMobileElement(String sObject) {
        return findMobileElement(sCurrentPage, sObject)
    }

    MobileElement findMobileElement(String sPage, String sObject) {
        return (MobileElement) findElement(sPage, sObject)
    }

    private WebElement searchElement(Object oPage, String sObject) {
        WebElement oElement = null

        Class<?> aClass = oPage.getClass()
        Class<?> aSuperClass = aClass.getSuperclass()
        List<Field> fields = new ArrayList<>()
        fields.addAll(Arrays.asList(aClass.getDeclaredFields()))
        fields.addAll(Arrays.asList(aSuperClass.getDeclaredFields()))

        fields.each { field ->

            if (field.getName().equalsIgnoreCase(sObject)) {
                try {
                   oElement =  field.get(oPage)
                } catch (Exception e ) {
                    logger.error(e.getMessage())
                }
            }
        }
        return oElement
    }

    List<WebElement> findElements(By oBy) {
        return oWebDriver.findElements(oBy)
    }

    List<WebElement> findElements(String sObject) {
        return findElements(sCurrentPage, sObject)
    }

    List<WebElement> findElements(String sPage, String sObject) {
        return searchElements(getPageInstance(sPage), sObject)
    }

    List<MobileElement> findMobileElements(By oBy) {
        return (List<MobileElement>) (List<?>) getAppiumDriver().findElements(oBy)
    }

    List<MobileElement> findMobileElements(String sObject) {
        return findMobileElements(sCurrentPage, sObject)
    }

    List<MobileElement> findMobileElements(String sPage, String sObject) {
        return (List<MobileElement>) (List<?>) searchElements(getPageInstance(sPage), sObject)
    }

    private List<WebElement> searchElements(Object oPage, String sObject) {
        List<WebElement> oElementList = null

        Class<?> aClass = oPage.getClass()
        Class<?> aSuperClass = aClass.getSuperclass()
        List<Field> fields = new ArrayList<>()
        fields.addAll(Arrays.asList(aClass.getDeclaredFields()))
        fields.addAll(Arrays.asList(aSuperClass.getDeclaredFields()))

        fields.each { field ->
            if(field.getName().equalsIgnoreCase(sObject)) {
                try {
                    oElementList = (List<WebElement>) field.get(oPage)
                } catch (Exception e) {
                    logger.error(e.getMessage())
                }
            }
        }
        return oElementList
    }

    void addData(String sKey, String sValue) {
        DataUtil.store(sKey, sValue)
    }

    void upDateData(String sKey, String sValue) {
        addData(sKey, sValue)
    }

    void clearDataByKey(String sKey) {
        DataUtil.remove(sKey)
    }

    String getDataByKey(String sKey) {
        DataUtil.retrieveString(sKey)
    }

    //TBD
    String getData(String sActualText) {
        return CoreUtil.process(sActualText)
    }

    void sleep(long iMilliSeconds) {
        Thread.sleep(iMilliSeconds)
    }

}