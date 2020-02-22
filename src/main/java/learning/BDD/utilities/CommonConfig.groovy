package learning.BDD.utilities

import learning.BDD.utilities.utilEnum.ApplicationType
import learning.BDD.utilities.utilEnum.BrowserType
import learning.BDD.utilities.utilEnum.WebDriverType
import org.apache.commons.lang3.StringUtils

class CommonConfig {

    private WebDriverType webDriverType
    private Integer webDriverTimeOut
    private Integer webDriverShortWait
    private WebDriverAuthentication webDriverAuthentication
    private ApplicationType applicationType
    private String sEnv
    private Boolean bScreenShotForAllSteps
    private Capability capability
    private RallyDev rallyDev

    WebDriverType getWebDriverType() {
        return webDriverType
    }

    void setWebDriverType(String webDriverType) {
        this.webDriverType = StringUtils.isEmpty(webDriverType) ? null : WebDriverType.valueOf(webDriverType)
    }

    Integer getWebDriverTimeOut() {
        return webDriverTimeOut
    }

    void setWebDriverTimeOut(Integer webDriverTimeOut) {
        this.webDriverTimeOut = webDriverTimeOut
    }

    Integer getWebDriverShortWait() {
        return webDriverShortWait
    }

    void setWebDriverShortWait(Integer webDriverShortWait) {
        this.webDriverShortWait = webDriverShortWait
    }

    WebDriverAuthentication getWebDriverAuthentication() {
        return webDriverAuthentication
    }

    void setWebDriverAuthentication(WebDriverAuthentication webDriverAuthentication) {
        this.webDriverAuthentication = webDriverAuthentication
    }

    void setWebDriverUsername(String name) {
        this.webDriverAuthentication.setUsername(name)
    }

    void setWebDriverPassword(String pass) {
        this.webDriverAuthentication.setPassword(pass)
    }

    ApplicationType getApplicationType() {
        return applicationType
    }

    void setApplicationType(String applicationType) {
        this.applicationType = StringUtils.isEmpty(applicationType) ? null : ApplicationType.valueOf(applicationType)
    }

    String getApplicationEnv() {
        return sEnv
    }

    void setApplicationEnv(String sEnv) {
        this.sEnv = sEnv
    }

    Boolean getScreenShotForAllSteps() {
        return bScreenShotForAllSteps
    }

    void setScreenShotForAllSteps(Boolean bScreenShotForAllSteps) {
        this.bScreenShotForAllSteps = bScreenShotForAllSteps
    }

    Capability getCapability() {
        return capability
    }

    void setCapability(Capability capability) {
        this.capability = capability
    }

    RallyDev getRallyDev() {
        return rallyDev
    }

    void setRallyDev(RallyDev rallyDev) {
        this.rallyDev = rallyDev
    }

    public class WebDriverAuthentication {
        private String username = "", password = ""

        String getUsername() {
            return username
        }

        void setUsername(String username) {
            this.username = username
        }

        String getPassword() {
            return password
        }


        void setPassword(String password) {
            this.password = password
        }
    }

    public class Capability {

        private String perfectoSessionID
        private String automationName
        private BrowserType browserName
        private String app
        private String deviceName
        private String platformName
        private String platformVersion

        String getPerfectoSessionID() {
            return perfectoSessionID
        }

        void setPerfectoSessionID(String perfectoSessionID) {
            this.perfectoSessionID = perfectoSessionID
        }

        String getAutomationName() {
            return automationName
        }

        void setAutomationName(String automationName) {
            this.automationName = automationName
        }

        BrowserType getBrowserName() {
            return browserName
        }

        void setBrowserName(String browserName) {
            this.browserName = StringUtils.isEmpty(browserName) ? null : BrowserType.valueOf(browserName)
        }

        String getApp() {
            return app
        }

        void setApp(String app) {
            this.app = app
        }

        String getDeviceName() {
            return deviceName
        }

        void setDeviceName(String deviceName) {
            this.deviceName = deviceName
        }

        String getPlatformName() {
            return platformName
        }

        void setPlatformName(String platformName) {
            this.platformName = platformName
        }

        String getPlatformVersion() {
            return platformVersion
        }

        void setPlatformVersion(String platformVersion) {
            this.platformVersion = platformVersion
        }
    }

    public class RallyDev {

        private String url
        private String apiKey
        private String projectName
        private String testSetToUploadResult

        String getUrl() {
            return url
        }

        void setUrl(String url) {
            this.url = url
        }

        String getApiKey() {
            return apiKey
        }

        void setApiKey(String apiKey) {
            this.apiKey = apiKey
        }

        String getProjectName() {
            return projectName
        }

        void setProjectName(String projectName) {
            this.projectName = projectName
        }

        String getTestSetToUploadResult() {
            return testSetToUploadResult
        }

        void setTestSetToUploadResult(String testSetToUploadResult) {
            this.testSetToUploadResult = testSetToUploadResult
        }
    }

}
