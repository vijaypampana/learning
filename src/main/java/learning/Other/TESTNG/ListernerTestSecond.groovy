package learning.Other.TESTNG

import org.testng.annotations.*
import org.testng.Assert

class ListernerTestSecond {

    @BeforeTest
    void beforeTest() {
       println("Before Test")
    }

    @AfterTest
    void AfterTest() {
        println("After Test")
    }

    @BeforeClass
    void beforeClass() {
        println("Before Class")
    }

    @AfterClass
    void AfterClass() {
        println("After Class")
    }

    @BeforeMethod
    void beforeMethod() {
        println("Before Method")
    }

    @AfterMethod
    void AfterMethod() {
        println("After Method")
    }

    @Test (priority = 0)
    void testCase1() {
        println("priority 0")
    }

    @Test
    void testCase2() {
        println("No Priority")
    }

    @Test (dependsOnMethods = ["testCase4", "testCase5"])
    void testCase3() {
        println("Depends on Method")
    }

    @Test (alwaysRun = true)
    void testCase4() {
        println("Test Case 4")
    }

    @Test
    void testCase5() {
        println("Test Case 5")
    }

    @Test
    void testCase6() {
        println("Test Case 6")
    }
}


//Priority = 0 will be executed First