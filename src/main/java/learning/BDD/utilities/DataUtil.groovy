package learning.BDD.utilities

class DataUtil {

    private static Map<String, Object> dataMap = new HashMap<>()

    public static void clear() {
        dataMap.clear()
    }

    public static Map<String, Object> get() {
        return dataMap
    }

}
