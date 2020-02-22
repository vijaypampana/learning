package learning.Other.TESTNG

import org.testng.Assert
import org.testng.SkipException
import org.testng.annotations.Test
import org.testng.Reporter

class ListernerTestFirst {

    private int i = 0, j = 0

    @Test
    void test1() {
        if(j<2) {
            Assert.assertTrue(false)
        }
        j++
    }

    @Test
    void test2() {
        throw new SkipException("I am skipping the test")
    }

    @Test(successPercentage = 60, invocationCount = 5)
    void test3() {
        if(i>2) {
            Reporter.log("Test Failed : $i")
            Assert.assertEquals(i, 8)
        }
    }

    @Test(enabled = false)
    void test4() {
        Reporter.log("Inside Test 4")
    }

}
