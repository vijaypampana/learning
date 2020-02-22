package learning.Other.TESTNG

import org.testng.IRetryAnalyzer
import org.testng.ITestResult
import org.testng.Reporter

class RetryAnalyzer implements IRetryAnalyzer {

    int counter = 0, retryLimit = 3
    @Override
    boolean retry(ITestResult iTestResult) {
        if(counter > 1)
            Reporter.log("Retrying")
        if(counter < retryLimit) {
            counter++
            return true
        }
        return false
    }
}
