package learning.BDD.utilities.mobile

import io.appium.java_client.MobileElement
import io.appium.java_client.pagefactory.AndroidFindBy
import io.appium.java_client.pagefactory.HowToUseLocators
import io.appium.java_client.pagefactory.iOSXCUITFindBy
import org.apache.commons.lang3.StringUtils

import static io.appium.java_client.pagefactory.LocatorGroupStrategy.CHAIN
import learning.BDD.utilities.Context

class CommonObjects {

    private Context context

    public CommonObjects() {
        this.context = Context.getInstance()
    }

    @HowToUseLocators(androidAutomation = CHAIN, iOSXCUITAutomation = CHAIN)
    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"com.android.packageinstaller:id/desc_container\" and not(starts-with(@bounds, \"[0,\"))]")
    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\" and not(starts-with(@bounds, \"[0,\"))]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert")
    public MobileElement Popup

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\" and not(starts-with(@bounds, \"[0,\"))]//android.widget.TextView[@clickable='false']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert//XCUIElementTypeStaticText")
    public List<MobileElement> popUpText

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\" and not(starts-with(@bounds, \"[0,\"))]//*[@class='android.widget.TextView' or @class='android.widget.Button) and @clickable='true']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert//XCUIElementTypeButton")
    public List<MobileElement> popUpButton



    void handlePopUp() {

        if(context.find(Popup)) {
            String popUpTextValue, popUpButtonTextValue
            popUpText.each { val ->
                if(context.find(val)) {
                    popUpTextValue += val.getAttribute(context.bIOS ? "label" : "text") + "\n"
                }
            }

            popUpButtonTextValue = POPUP.values().find{ it.popUpText == popUpTextValue}.popUpButton

            popUpButton.each { val ->
                if(context.find(val) && popUpButtonTextValue.equalsIgnoreCase(val.getAttribute(context.bIOS() ? "label" : "text"))) {
                    val.click()
                }
            }
        }

    }

    void handlePopUp(POPUP popup) {
        if(context.find(Popup)) {
            String popUpTextValue, popUpButtonValue
            popUpText.each { val ->
                popUpTextValue += val.getAttribute(context.bIOS? "label" : "text") + "\n"
            }
            if(popUpTextValue.matches("(.*)" + popup.getPopUpText() + "(.*)")) {
                popUpButtonValue = popup.getPopUpButton()
            }

            popUpButton.each { val ->
                if(popUpButtonValue.equalsIgnoreCase(context.bIOS ? "label" : "text")) {
                    val.click()
                    return null
                }
            }
        }
    }

    public enum POPUP {

        ERROR_POPUP(
                "We cannot process request at this time",
                "OK",
                "",
                ""
        ),
        LOCATION_PERMISSION_POPUP(
                "Allow \"myCigna\" to access your location while you are using the app?",
                "Allow",
                "Allow \"myCignaQA\" to access your location while you are using the app?",
                "ALLOW"
        ),
        IOS_NOTIFICATION_PERMISSION_POPUP(
                "Would Like to send you Notifications",
                "Allow",
                "",
                ""
        ),
        ANDROID_CRASH_POPUP(
                "",
                "",
                "A Crash has been detected",
                "SEND"
        )

        private String popUpTextIOS, popUpTextAndroid, popUpButtonIOS, popUpButtonAndroid, popUpText, popUpButton

        POPUP(String popUpTextIOS, String popUpTextAndroid, String popUpButtonIOS, String popUpButtonAndroid) {
            this.popUpTextIOS = popUpTextIOS
            this.popUpTextAndroid = popUpTextAndroid
            this.popUpButtonIOS = popUpButtonIOS
            this.popUpButtonAndroid = popUpButtonAndroid
        }

        String getPopUpText() {
            return popUpText
        }

        void setPopUpText(String popUpText) {
            this.popUpText = Context.getInstance().bIOS ? popUpTextIOS : StringUtils.isEmpty(popUpTextAndroidAndroid) ? popUpTextIOS : popUpTextAndroid
        }

        String getPopUpButton() {
            return popUpButton
        }

        void setPopUpButton(String popUpButton) {
            this.popUpButton = Context.getInstance().bIOS ? popUpButtonIOS : StringUtils.isEmpty(popUpButtonAndroid) ? popUpButtonIOS : popUpButtonAndroid
        }
    }

}
