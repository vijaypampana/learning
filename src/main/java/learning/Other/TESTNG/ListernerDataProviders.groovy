package learning.Other.TESTNG

import org.testng.annotations.*

class ListernerDataProviders {

    @DataProvider
    Object[][] testData1() {
        Object[][] data = new Object[2][2]
        data[0][0] = "test1"; data[0][1] = "test2"; data[1][0] = "test3"; data[1][1] = "test4"
        return data
    }

    @DataProvider
    Object[][] testData2() {
        Object[][] data = new Object[3][3]
        data[0][0] = "test00"; data[0][1] = "test01"; data[0][2] = "test02";
        data[1][0] = "test10"; data[1][1] = "test11"; data[1][2] = "test12";
        data[2][0] = "test20"; data[2][1] = "test21"; data[2][2] = "test22";
        return data
    }

    @BeforeClass
    void beforeClass() {
        println("1BC")
    }

    @AfterClass
    void afterClass() {
        println("1AC")
    }

    @BeforeMethod
    void beforeMethod() {
        println("1BM")
    }

    @AfterMethod
    void afterMethod() {
        println("1AM")
    }

    @Test(dataProvider = "testData1")
    void DPTest(String str1, String str2) {
        println(str1+str2)
    }

    @Test(dataProvider = "testData2")
    void DPTest(String str1, String str2, String str3) {
        println(str1+str2+str3)
    }

    @Test
    @Parameters(["data1","data2"])
    void testNGParam(@Optional("default") String str1, String str2) {
        println(str1+str2)
    }

    @Test(priority = 1)
    void testcase1() {
        println("Priority 11")
    }

    @Test(priority = 2)
    void testcase2() {
        println("Priority 12")
    }

    @Test(priority = 3)
    void testcase3() {
        println("Priority 13")
    }

    @Test(priority = 1)
    void testcase4() {
        println("Priority 21")
    }

    @Test(priority = 2)
    void testcase5() {
        println("Priority 22")
    }

    @Test(priority = 3)
    void testcase6() {
        println("Priority 23")
    }

}

