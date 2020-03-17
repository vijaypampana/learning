package learning.BDD.utilities.util;

import learning.BDD.utilities.Context;
import learning.BDD.utilities.util.enums.DateUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreUtil {

    private static final String MY_PATTERN = "\\[[^\\[\\]]+]";

    public static String process(String value) {
        Matcher matcher = Pattern.compile(MY_PATTERN).matcher(value);
        if(matcher.find()) {
            String replacement = String.valueOf(getReplacement(matcher.group()));
            return process(matcher.replaceFirst(replacement.replace("$", "\\$")));
        } else {
            return decode(value);
        }
    }

    private static String getReplacement(String value) {
        value = decode(value);

        //Matching API Response
        Matcher matcher = Pattern.compile(DataUtil.getAPIPattern()).matcher(value);
        if(matcher.find()) {
            return DataUtil.queryApiResponse(matcher.group(1), matcher.group(2));
        }

        //Matching DateUtil
        matcher = Pattern.compile(DateUtil.getDatePattern()).matcher(value);
        if(matcher.find()) {
            return DateUtil.valueOf(matcher.group(1)).getDate(matcher.group(2));
        }

        //Matching DateUtil Pattern
        matcher = Pattern.compile(DateUtil.getFormatPattern()).matcher(value);
        if(matcher.find()) {
            return DateUtil.getDate(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        //Matching Currency Util
        matcher = Pattern.compile(CurrencyUtil.getPattern()).matcher(value);
        if(matcher.find()) {
            return CurrencyUtil.formatCurrency(matcher.group(1), matcher.group(2));
        }

        //Matching Phone Number Util
        matcher = Pattern.compile(PhoneNumberUtil.getPattern()).matcher(value);
        if(matcher.find()) {
            return PhoneNumberUtil.formatPhoneNumber(matcher.group(1), matcher.group(2));
        }

        /*
        //Matching Random util
        matcher = Pattern.compile(RandomUtil.getPattern()).matcher(value);
        if(matcher.find()) {
            if(matcher.groupCount() == 2) {
                return RandomUtil.valueOf(matcher.group(1)).getRandom(matcher.group(2));
            } else if(matcher.groupCount() == 3) {
                return RandomUtil.valueOf(matcher.group(1)).storeRandom(matcher.group(2), matcher.group(3));
            }
        }






        //Matching Data Transform
        matcher = Pattern.compile(DataTransformUtil.getPattern()).matcher(value);
        if(matcher.find()) {
            return DataTransformUtil.valueOf(matcher.group(1)).getTransformedValue(matcher.group(2));
        }





        //Matching MobileText by OS
        matcher = Pattern.compile(MobileUtil.getMobilePattern()).matcher(value);
        if(matcher.find() && Context.getInstance().isMobile()) {
            return Context.getInstance().isbIOS()? matcher.group(1) : matcher.group(2);
        }
        */
        return DataUtil.contains(value) ? DataUtil.retrieveString(value) : encode(value);

    }

    private static String encode(String value) {
        return value.replaceAll("\\[", "~(~").replaceAll("]", "~)~");
    }

    private static String decode(String value) {
        return value.replaceAll("~\\(~", "[").replaceAll("~\\)~", "]");
    }


}
