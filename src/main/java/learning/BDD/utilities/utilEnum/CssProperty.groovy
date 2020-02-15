package learning.BDD.utilities.utilEnum

enum CssProperty {

    COLOR("color"),
    BACKGROUND_COLOR("background-color"),
    FONT_SIZE("font-size"),
    TEXT_ALIGN("text-align")

    private final String sProperty

    String getProperty() {
        return sProperty
    }

    CssProperty(String sProperty) {
        this.sProperty = sProperty
    }
}