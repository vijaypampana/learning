package learning.BDD.utilities.util;

import java.text.NumberFormat;
import java.util.Locale;

public enum CurrencyUtil {

    USD(Locale.US),
    YEN(Locale.JAPAN),
    YUAN(Locale.CHINA),
    EURO(Locale.UK);

    Locale chars;

    CurrencyUtil(Locale chars) {
        this.chars = chars;
    }

    public Locale getChars() {
        return chars;
    }

    public static String getPattern() {
        return "^\\[(USD|YEN|YUAN|EURO)\\~\\~(.*?)]$";
    }

    public static String formatCurrency(String sFormatType, String sCurrency) {
        try {
            return NumberFormat.getCurrencyInstance(valueOf(sFormatType).getChars()).format(Double.parseDouble(sCurrency.replaceAll("[^0-9.]", "")));
        } catch (Exception ignored) {

        }
        return "";
    }

}
