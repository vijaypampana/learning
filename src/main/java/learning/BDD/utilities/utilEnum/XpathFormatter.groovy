package learning.BDD.utilities.utilEnum

import learning.BDD.utilities.Context

class XpathFormatter {

    static String getTranslatedQuotes(String sText) {
        return sText.contains("\"") ? "'" : "\""
    }

    static TEXTPROPERTYMAPPER getPlatformBasedProperty() {
        return Context.getInstance().isbIOS() ? TEXTPROPERTYMAPPER.IOS : TEXTPROPERTYMAPPER.ANDROID
    }

    public enum TEXTPROPERTYMAPPER {
        IOS("@label"),
        ANDROID("@text")

        String sProperty;

        TEXTPROPERTYMAPPER(String sProperty) {
            this.sProperty = sProperty
        }

        String getsProperty() {
            return sProperty
        }

        void setsProperty(String sProperty) {
            this.sProperty = sProperty
        }
    }

    public enum LABELVALIDATIONFORMATTER {
        TEXTEQUALS("//%s[%s=%s%s%s]", "*", "*"),
        TEXTCONTAINS("//%s[contains(%s,%s%s%s)]", "*", "*"),
        BUTTONEQUALS("//%s[%s=%s%s%s]", "XCUIElementTypeButton", "*"),
        BUTTONCONTAINS("//%s[contains(%s,%s%s%s)]", "XCUIElementTypeButton", "*")

        private String xpathFormat, iosClass, androidClass

        LABELVALIDATIONFORMATTER(String xpathFormat, String iosClass, String androidClass) {
            setXpathFormat(xpathFormat)
            setIosClass(iosClass)
            setAndroidClass(androidClass)
        }

        String getPropertyBasedOnPlatform() {
            return getPlatformBasedProperty().getProperty()
        }

        String getXpath(String text) {
            return String.format(this.getXpathFormat(), getClassBasedOnPlatform(), getPropertyBasedOnPlatform(), getTranslatedQuotes(text), text, getTranslatedQuotes(text))
        }

        String getXpathFormat() {
            return xpathFormat
        }

        void setXpathFormat(String xpathFormat) {
            this.xpathFormat = xpathFormat
        }

        String getIosClass() {
            return iosClass
        }

        void setIosClass(String iosClass) {
            this.iosClass = iosClass
        }

        String getAndroidClass() {
            return androidClass
        }

        private String getClassBasedOnPlatform() {
            return Context.getInstance().isbIOS() ? getIosClass() : getAndroidClass()
        }

        void setAndroidClass(String androidClass) {
            this.androidClass = androidClass
        }
    }
}
