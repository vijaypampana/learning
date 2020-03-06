package learning.BDD.utilities.mobile;

import cucumber.api.java.en.Given;
import io.appium.java_client.AppiumDriver;
import io.restassured.RestAssured;
import learning.BDD.utilities.Context;
import learning.BDD.utilities.mobile.CommonObjects;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Login extends CommonObjects {

    private Context context;
    private Environment env;
    private AppiumDriver oDriver;

    public Login() {
        if(context == null) {
            context = Context.getInstance();
            oDriver = context.getAppiumDriver();
        }
    }

    @Given("^I start my application$")
    void startApplication() {
        if(!this.context.isAPI()) {
            this.context.clearPageInstance();
            oDriver.closeApp();
            oDriver.launchApp();
            this.handlePopUp(this.context.isbIOS() ? POPUP.IOS_NOTIFICATION_PERMISSION_POPUP : POPUP.ANDROID_CRASH_POPUP);
        }
        setEnvironment();
    }

    @Given("^I set application Environment$")
    void setEnvironment() {
        if(StringUtils.isNotEmpty(env.name())) {
            changeEnvironment(env);
        }
    }

    @Given("^I change application environment to \"(.*)\"$")
    void changeEnvironment(Environment oEnv) {
        if(!context.isAPI()) {

        }
        context.setsHost(oEnv.getsAPIHost());
        context.setsBasePath(env.getsApiJunction());
    }

    @Given("^I navigate to \"(.*)\" screen$")
    void deepLinkNavigate(String sModule) {
        deepLinksLogin(sModule, new HashMap<>());
    }


    void deepLinksLogin(String sModule, String sUserName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("env", env.getsEnv());
        params.put("username", sUserName);
        params.put("password", "test1234");

        deepLinksLogin(sModule, params);
    }

    void deepLinksLogin(String deepLinkName, Map<String, String> params) {
        context.setsHost(env.getsAPIHost());
        apiLogin(params.get("username"));

        if(StringUtils.isNotEmpty(deepLinkName)) {
            DeepLinks deepLinks = DeepLinks.valueOf(DeepLinks.getEnum(deepLinkName));
            String deepLinkUrl = deepLinks.getUrl(new LinkedHashMap<>(params));

            oDriver.get(deepLinkUrl);

            context.setCurrentPage(deepLinks.getLandingPage());
            context.getWebDriverWait().until(ExpectedConditions.visibilityOf(context.findElement("WaitElement")));
        }
    }

    void apiLogin(String sUserName) {

        context.setsHost(env.getsAPIHost());
        String token = RestAssured.given()
                .param("client_id", "de_client_1")
                .param("grant_type", "password")
                .param("username", sUserName)
                .param("password", "cigna123")
                .when()
                .post(EndPoint.valueOf("LOGIN_POST").getUrl())
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getString("access_token");

        context.addData("API_TOKEN", "Bearer " + token);

        String jwtToken = RestAssured.given().header(HttpHeaders.AUTHORIZATION, context.getData("API_TOKEN"))
                                .when().get(EndPoint.valueOf("JWT_GET").getUrl()).then().statusCode(200).extract().jsonPath().getString("access_token");

        context.addData("JWT_TOKEN", "Bearer " + jwtToken);

    }

    public static enum EndPoint {
        JWT_GET("/mga/sps/oauth/oauth20/token"),
        LOGIN_POST("/mga/sps/oauth/oauth20/token");

        private String url;

        EndPoint(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }


    public static enum Environment {

        DEV01("https://a-mobile-api.cigna.com", "/d1-mobile", "DEV01"),
        ACC01("https://a-mobile-api.cigna.com", "/mobile", "ACC01"),
        PROD("https://mobile-api.cigna.com", "/mobile", "PROD");

        private String sAPIHost;
        private String sApiJunction;
        private String sEnv;

        Environment(String sAPIHost, String sApiJunction, String sEnv) {
            this.sAPIHost = sAPIHost;
            this.sApiJunction = sApiJunction;
            this.sEnv = sEnv;
        }

        public String getsAPIHost() {
            return sAPIHost;
        }

        public String getsApiJunction() {
            return sApiJunction;
        }

        public String getsEnv() {
            return sEnv;
        }
    }

}
