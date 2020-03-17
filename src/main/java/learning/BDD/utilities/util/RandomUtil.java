package learning.BDD.utilities.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public enum RandomUtil {

    ALPHA(Chars.ALPHA.getChars()),
    NUMERIC(Chars.NUMERIC.getChars()),
    SPECIAL(Chars.SPECIAL.getChars()),
    ALPHA_NUMERIC(Chars.ALPHA.getChars().concat(Chars.NUMERIC.getChars())),
    ALPHA_SPECIAL(Chars.ALPHA.getChars().concat(Chars.SPECIAL.getChars())),
    NUMERIC_SPECIAL(Chars.SPECIAL.getChars().concat(Chars.NUMERIC.getChars())),
    ALPHA_NUMERIC_SPECIAL(Chars.ALPHA.getChars().concat(Chars.NUMERIC.getChars()).concat(Chars.SPECIAL.getChars()));

    public static String getPattern() {
        return "^\\[(ALPHA|NUMERIC|SPECIAL|ALPHA_NUMERIC|ALPHA_SPECIAL|NUMERIC_SPECIAL|ALPHA_NUMERIC_SPECIAL)\\~\\~(\\d*)(?:\\~\\~(.*?))?]$";
    }

    public String getRandom(String length) {
        return storeRandom(length, null);
    }

    public String storeRandom(String length, String key) {
        String randomChars = "";

        //This method will random generates a number between 0 to getChars.length() and concatenate the char at that position to random string until i = length
        for(int i=0; i < Integer.parseInt(length); i++) {
            randomChars = randomChars.concat(String.valueOf(getChars().charAt(new Random().nextInt(getChars().length()))));
        }

        if(StringUtils.isNotEmpty(key)) {
            DataUtil.store(key, randomChars);
        }
        return randomChars;
    }

    RandomUtil(String chars) {
        this.chars = chars;
    }
    String chars;

    public String getChars() {
        return chars;
    }

    enum Chars {
        ALPHA("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),
        NUMERIC("1234567890"),
        SPECIAL("!@#%^&(");

        String chars;
        Chars(String chars) {
            this.chars = chars;
        }

        public String getChars() {
            return chars;
        }
    }

}
