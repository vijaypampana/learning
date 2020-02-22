package learning.collections

import org.testng.annotations.Test

class CollectionsMap {

    @Test
    void test() {
        //Map<String, String> testHashMap = new HashMap<>()
        Map<String, String> testHashMap = new LinkedHashMap<>()
        testHashMap.put("User1", "Test")
        testHashMap.put("User2", "Vijay")
        testHashMap.put("User3", "Deepika")
        testHashMap.put("User4", "Sanvika")
        testHashMap.forEach({k,v -> println("Key is $k and the Value is $v")})
        testHashMap.remove("User1")
        testHashMap.entrySet().each { me ->
            println(me.key + " : " + me.value)
        }
    }
}
