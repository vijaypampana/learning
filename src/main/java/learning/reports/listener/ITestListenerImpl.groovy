package learning.reports.listener

import com.aventstack.extentreports.ExtentReports
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult

class ITestListenerImpl extends ExtentReportListener implements ITestListener {

    private static ExtentReports extent

    @Override
    void onTestStart(ITestResult iTestResult) {

    }

    @Override
    void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    void onTestFailure(ITestResult iTestResult) {

    }

    @Override
    void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    void onStart(ITestContext iTestContext) {
        extent = setup()
    }

    @Override
    void onFinish(ITestContext iTestContext) {
        extent.flush()

    }
}
