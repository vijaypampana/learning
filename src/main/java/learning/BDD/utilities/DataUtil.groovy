package learning.BDD.utilities

import io.restassured.response.ValidatableResponse

import java.util.regex.Matcher
import java.util.regex.Pattern

class DataUtil {

    private static Map<String, Object> dataMap = new HashMap<>()

    public static void clear() {
        dataMap.clear()
    }

    public static Map<String, Object> get() {
        return dataMap
    }

    private static long getThreadId() {
        return Thread.currentThread().getId()
    }

    //Not sure what this method will do
    private static String parseKey(String skey) {
        skey = skey.replace(" ", "")
        Matcher matcher = Pattern.compile("\\[(.*?)]\$").matcher(skey)
        if(matcher.find()) {
            return getThreadId() + "." + matcher.group(1)
        } else {
            return getThreadId() + "." + skey
        }
    }

    public static boolean contains(String key) {
        return dataMap.containsKey(parseKey(key))
    }

    public static void store(String key, Object value) {
        dataMap.put(parseKey(key), value)
    }

    public static void remove(String key) {
        dataMap.remove(parseKey(key))
    }

    public static Object retrieve(String key) {
        return dataMap.get(parseKey(key))
    }

    public static String retrieveString(String key) {
        Object val = retrieve(key)
        return val == null ? "" : String.valueOf(val)
    }

    public static retrieveBoolean(String key) {
        return Boolean.parseBoolean(retrieveString(key))
    }

    //If the key starts with @ ignore first char other wise take the total key
    public static ValidatableResponse retrieveValidatableResponse(String key) {
        return (ValidatableResponse) retrieve(key.startsWith("@") ? key.substring(1) : key)
    }

    public static String getAPIPattern() {
        return "^\\[(@.*?)\\~\\~(.*?)\\]\$"
    }

    //This method will transverse through the response body and retrieve the query response
    public static String queryApiResponse(String key, String query) {
        if(retrieveValidatableResponse(key).extract().contentType().toLowerCase().contains("xml")) {
            return retrieveValidatableResponse(key).extract().body().xmlPath().getShort(query)
        } else if(retrieveValidatableResponse(key).extract().contentType().toLowerCase().contains("json")) {
            return retrieveValidatableResponse(key).extract().body().jsonPath().getString(query)
        } else if(retrieveValidatableResponse(key).extract().contentType().toLowerCase().contains("html")) {
            return retrieveValidatableResponse(key).extract().body().htmlPath().getString(query)
        } else
            return null
    }

}
