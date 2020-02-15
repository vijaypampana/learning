package learning.BDD.utilities.transformer

import cucumber.api.Transformer

class TransformToCssColor extends Transformer<String> {
    @Override
    String transform(String s) {
        if(s.startsWith("#")) {
            return hexToRGB(s.toUpperCase())
        } else {
            return rgbToHex(s)
        }
    }

    String hexToRGB(String sColor) {

        Integer r = Integer.valueOf(sColor.substring(1,3), 16)
        Integer g = Integer.valueOf(sColor.substring(3,5), 16)
        Integer b = Integer.valueOf(sColor.substring(5,7), 16)

        return "rgba(" + r +", " + g + ", " + b + ", 1)"
    }

    String rgbToHex(String sColor) {
        //TBD
        return sColor
    }
}