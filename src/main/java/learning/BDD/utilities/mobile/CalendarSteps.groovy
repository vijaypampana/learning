package learning.BDD.utilities.mobile

import cucumber.api.java.en.Given
import io.appium.java_client.MobileElement
import learning.BDD.utilities.Context
import org.openqa.selenium.By
import org.openqa.selenium.Keys

import java.text.ParseException
import java.text.SimpleDateFormat

class CalendarSteps {

    private Context context

    public CalendarSteps() {
        if(context == null) {
            context = Context.getInstance()
        }
    }

    @Given("^I select Date as \"([^\"]*)\"\$")
    void date_select(String oDate) throws ParseException {
        if(context.bIOS) {
            selectIOSDate(oDate)
        } else {
            try {
                selectAndroidDateUsingPicker(oDate)
            } catch (NoSuchElementException ignored) {
                selectAndroidDateUsingCalendar(oDate)
            }
        }
    }

    void selectIOSDate(String oDate) {
        context.findMobileElement(By.xpath("//XCUIElementTypeDatePicker//XCUIElementTypePickerWheel[3]")).sendKeys(new SimpleDateFormat("yyyy").format(oDate))
        context.findMobileElement(By.xpath("//XCUIElementTypeDatePicker//XCUIElementTypePickerWheel[1]")).sendKeys(new SimpleDateFormat("MMMM").format(oDate))
        context.findMobileElement(By.xpath("//XCUIElementTypeDatePicker//XCUIElementTypePickerWheel[2]")).sendKeys(new SimpleDateFormat("d").format(oDate))
        context.findMobileElement(By.xpath("//XCUIElementTypeDatePicker//XCUIElementTypePickerWheel[1]")).sendKeys(new SimpleDateFormat("MMMM").format(oDate))
        context.findMobileElement(By.xpath("//XCUIElementTypeButton[@label=\"Done\"]")).click()
    }

    void selectAndroidDateUsingPicker(String oDate) {
        context.findMobileElement(By.xpath("//android.widget.NumberPicker[3]/android.widget.EditText")).setValue(new SimpleDateFormat("yyyy").format(oDate))
        context.findMobileElement(By.xpath("//android.widget.NumberPicker[1]/android.widget.EditText")).setValue(new SimpleDateFormat("MMM").format(oDate))
        context.findMobileElement(By.xpath("//android.widget.NumberPicker[2]/android.widget.EditText")).setValue(new SimpleDateFormat("dd").format(oDate))
        context.findMobileElement(By.xpath("//android.widget.NumberPicker[1]/android.widget.EditText")).sendKeys(Keys.TAB)
        context.findMobileElement(By.xpath("//android.widget.TextView[contains(@resource-id,\"id/ok\")]")).click()
    }

    void selectAndroidDateUsingCalendar(String oDate) {

        Integer iYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(oDate))
        Integer iMonth = Integer.parseInt(new SimpleDateFormat("M").format(oDate))
        Integer iYearStart = 0, iYearEnd = 0, firstRun = 0
        List<MobileElement> oElementList = null
        MobileElement oPreviousMonth, oNextMonth

        //Selecting Year
        context.findMobileElement(By.xpath("//android.widget.TextView[contains(@resource-id, \"id/date_picker_header_year\")]")).click()

        while(iYear < iYearStart || iYear > iYearEnd || firstRun == 0) {
            firstRun++
            MobileElement oFirstElement, oLastElement
            oElementList = context.findMobileElements(By.xpath("//android.widget.TextView[@contains(@resource-id, \"id/text1\")]"))
            oFirstElement = oElementList.get(0)
            oLastElement = oElementList.get(oElementList.size() - 1)
            iYearStart = Integer.parseInt(oFirstElement.getText())
            iYearEnd = Integer.parseInt(oLastElement.getText())
            if (iYear > iYearEnd) {
                AppiumSteps.swipe(oLastElement, oFirstElement)
            } else if (iYear < iYearStart) {
                AppiumSteps.swipe(oFirstElement, oLastElement)
            }
        }

            context.findMobileElement(By.xpath("//android.widget.TextView[contains(@resource-id,\"id/text1\") and @text=\"" + iYear + "\"]")).click()

            oPreviousMonth = context.findMobileElement(By.xpath("//android.widget.ImageButton[contains(@resource-id, \"id/prev\")]"))
            oNextMonth = context.findMobileElement(By.xpath("//android.widget.ImageButton[contains(@resource-id, \"id/next\")]"))
            iMonth = iMonth - 6
            if(iMonth > 0) {
                for(int i=0; i < iMonth; i++) {
                    oNextMonth.click()
                }
            } else {
                for(int i=0; i < iMonth; i++) {
                    oPreviousMonth.click()
                }
            }

        context.findMobileElement(By.xpath("//android.view.View/android.view.View[@text=\"" + new SimpleDateFormat("d").format(oDate) + "\"]")).click()
        context.findMobileElement(By.xpath("//android.widget.Button[contains(@resource-id,\"id/button1\")]")).click()

        }
}
