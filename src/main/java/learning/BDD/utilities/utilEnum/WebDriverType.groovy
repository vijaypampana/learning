package learning.BDD.utilities.utilEnum

enum WebDriverType {

    API("", "", "", ""),
    PERFECTO("https", "perfectomobile.com", "", "/nexperience/perfectomobile/wd/hub"),
    BROWSERSTACK("https", "hub-cloud.browserstack.com", "", "/wd/hub"),
    APPIUM("http", "localhost", "4723", "/wd/hub"),
    ZALENIUM("http", "zalenium", "", "/wd/hub")



    private final String sProtocol
    private final String sHost
    private final String sPort
    private final String sPath

    WebDriverType( String sProtocol, String sHost, String sPort, String sPath) {
        this.sProtocol = sProtocol
        this.sHost = sHost
        this.sPort = sPort
        this.sPath = sPath
    }

    public String getHost() {
        return sHost
    }

    public URL getURL() {
        URL url = null
        try {
            if(sPort.isEmpty()) {
                if(sProtocol.equalsIgnoreCase("https")) {
                    url = new URL(sProtocol + "://" + sHost + ":" + ":443" + sPath)
                } else {
                    url = new URL(sProtocol + "://" + sHost + ":" + ":80" + sPath)
                }
            } else {
                url = new URL(sProtocol + "://" + sHost + ":" + sPort + sPath)
            }
        } catch (Exception e) {
            e.printStackTrace()
        }

        return url
    }
}