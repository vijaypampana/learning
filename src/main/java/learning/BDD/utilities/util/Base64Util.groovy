package learning.BDD.utilities.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser

//This class work on Base64 data
class Base64Util {

    public static byte[] getAsBytes(String base64Value) {
        return Base64.getMimeDecoder().decode(base64Value);
    }

    public static String getAsString(String base64Value) {
        return new String(getAsBytes(base64Value));
    }

    public static JsonObject getAsJsonObject(String base64Value) {
        return new JsonParser().parse(getAsString(base64Value)).getAsJsonObject();
    }

}
