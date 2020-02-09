package learning.BDD.utilities.utilEnum

enum ApplicationType {

    MyCigna (
            "com.cigna.mobile.myCigna.pushTest",
            "com.cigna.mobile.mycigna.debug",
            "com.cigna.mycigna.androidui.activity.welcomeActivity"
    )

    private final String sIOSBundleID
    private final String sAndroidPackage
    private final String sAndroidActivity

    ApplicationType(String sIOSBundleID, String sAndroidPackage, String sAndroidActivity) {
        this.sIOSBundleID = sIOSBundleID
        this.sAndroidPackage = sAndroidPackage
        this.sAndroidActivity = sAndroidActivity
    }

    String getsIOSBundleID() {
        return sIOSBundleID
    }

    String getsAndroidPackage() {
        return sAndroidPackage
    }

    String getsAndroidActivity() {
        return sAndroidActivity
    }
}