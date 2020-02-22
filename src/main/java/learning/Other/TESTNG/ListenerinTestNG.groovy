package learning.Other.TESTNG

import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult
import org.testng.Reporter

class ListenerinTestNG implements ITestListener {

    @Override
    void onTestStart(ITestResult iTestResult) {
        Reporter.log("Starting Test : " + getMethodName(iTestResult))
    }

    @Override
    void onTestSuccess(ITestResult iTestResult) {
        Reporter.log("Test Success : " + getMethodName(iTestResult))
    }

    @Override
    void onTestFailure(ITestResult iTestResult) {
        Reporter.log("Test Failed : " + getMethodName(iTestResult))
    }

    @Override
    void onTestSkipped(ITestResult iTestResult) {
        Reporter.log("Test Skipped : " + getMethodName(iTestResult))
    }

    @Override
    void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Reporter.log("Test is passed based on percentage : " + getMethodName(iTestResult))
    }

    @Override
    void onStart(ITestContext iTestContext) {
        Reporter.log("Test Execution Started")
    }

    @Override
    void onFinish(ITestContext iTestContext) {
        Reporter.log("Test Execution Finished")
    }

    private String getMethodName(ITestResult result) {
        return result.getMethod().getMethodName()
    }
}
