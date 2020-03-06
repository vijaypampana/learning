package learning.BDD.utilities.mobile;

import java.util.LinkedHashMap;

public enum DeepLinks {
    HOME("mycigna://dashboard", "Home.Landing"),
    CLAIMS_AND_EOBS("mycigna://claims", "Claims.Landing"),
    PROFILE_AND_SETTINGS("mycigna://userpreferences", "ProfileAndSettings.Landing");

    public static String getEnum(String actualEnum) {
        return actualEnum.toUpperCase().replace(" ", "_").replace("/", "_").replace("//", "_").replace("&", "AND");
    }

    //API parameters
    public String getUrl(LinkedHashMap<String, String> params) {
        StringBuilder parameters = new StringBuilder();
        params.keySet().forEach( val ->
         parameters.append(val).append("=").append(params.get(val)).append("&"));
        if(getsURL().endsWith("?") || getsURL().endsWith("&")) {
            return getsURL() + (parameters.toString().isEmpty() ? "" : parameters.substring(0, parameters.length()-1));
        } else {
            return getsURL() + (parameters.toString().isEmpty() ? "" : "?" + parameters.substring(0, parameters.length()-1));
        }
    }

    private String sURL, landingPage;

    DeepLinks(String sURL, String landingPage) {
        this.sURL = sURL;
        this.landingPage = landingPage;
    }

    public String getsURL() {
        return sURL;
    }

    public String getLandingPage() {
        return landingPage;
    }
}
