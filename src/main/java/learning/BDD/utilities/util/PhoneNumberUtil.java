package learning.BDD.utilities.util;

import learning.BDD.utilities.Context;

import javax.swing.text.MaskFormatter;

public enum PhoneNumberUtil {

    PHONENUMBER_HYPEN("###-###-####"),
    PHONENUMBER_HYPEN_AR("(###) ###-####"),
    PHONENUMBER_SPACE("(###) ### ####"),
    PHONENUMBER_HYPEN_EXTN("# (###) ###-####"),
    PHONENUMBER_SPACE_EXTN("# (###) ### ####");

    PhoneNumberUtil(String chars) {
        this.chars = chars;
    }
    String chars;

    public String getChars() {
        return chars;
    }

    public static String getPattern() {
        return "^\\[(PHONENUMBER_HYPEN|PHONENUMBER_HYPEN_AR|PHONENUMBER_SPACE|PHONENUMBER_HYPEN_EXTN|PHONENUMBER_SPACE_EXTN)\\~\\~(.*?)]$";
    }


    public static String formatPhoneNumber(String sFormatType, String sPhoneNumber) {
        String sValue = "";
        try {
            MaskFormatter maskFormatter = new MaskFormatter(String.valueOf(valueOf(sFormatType).getChars()));
            maskFormatter.setValueContainsLiteralCharacters(false);
            sValue = maskFormatter.valueToString(Context.getInstance().getData(sPhoneNumber.replaceAll("[^0-9]", "")));
        }catch (Exception ignored) {
        }
        return sValue;
    }
}
