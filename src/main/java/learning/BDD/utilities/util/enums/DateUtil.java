package learning.BDD.utilities.util.enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DateUtil {


    TODAY(0,0,0),
    YESTERDAY(-1, 0, 0),
    TOMORROW(1, 0, 0),
    NEXT_MONTH(0,1,0),
    PREVIOUS_MONTH(0,-1, 0),
    NEXT_YEAR(0, 0, 1),
    PREVIOUS_YEAR(0, 0, -1);

    private int daysToAdd, monthsToAdd, yearsToAdd, daysToOverride, monthsToOverride, yearsToOverride;
    DateUtil(int daysToAdd, int monthsToAdd, int yearsToAdd) {
        this.daysToAdd = daysToAdd;
        this.monthsToAdd = monthsToAdd;
        this.yearsToAdd = yearsToAdd;
    }

    public static String DatePattern = "\\[(TODAY|YESTERDAY|TOMORROW|NEXT_MONTH|PREVIOUS_MONTH|NEXT_YEAR|PREVIOUS_YEAR)\\~~(.*?)]$";
    public static String formatPattern = "\\[(.*?)\\~\\~(.*?)\\~\\~(.*?)\\]$";
    public static String overridePattern = "([\\+|-]?\\d{1,3})([D|M|Y])";

    public static String getDatePattern() {
        return DatePattern;
    }

    public static String getFormatPattern() {
        return formatPattern;
    }

    public static String getOverridePattern() {
        return overridePattern;
    }

    public int getDaysToAdd() {
        return daysToAdd;
    }

    public int getMonthsToAdd() {
        return monthsToAdd;
    }

    public int getYearsToAdd() {
        return yearsToAdd;
    }

    public static String getDate(String date, String actualFormat, String desiredFormat) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(actualFormat)).format(DateTimeFormatter.ofPattern(desiredFormat));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getDate(String format) {
        daysToOverride = getDaysToAdd();
        monthsToOverride = getMonthsToAdd();
        yearsToOverride = getYearsToAdd();
        format = override(format);
        return LocalDateTime.now().plusDays(daysToOverride).plusMonths(monthsToOverride).plusYears(yearsToOverride).format(DateTimeFormatter.ofPattern(format));
    }

    private String override(String format) {
        Matcher matcher = Pattern.compile(getOverridePattern()).matcher(format);
        if(matcher.find()) {
            update(DatePart.valueOf(matcher.group(2)), Integer.parseInt(matcher.group(1)));
            return override(matcher.replaceFirst(""));
        }
        return format;
    }

    private void update(DatePart datePart, int value) {
        switch (datePart) {
            case D:
                daysToOverride += value;
                break;
            case M:
                monthsToOverride += value;
                break;
            case Y:
                yearsToOverride += value;
                break;
        }
    }

    public enum DatePart {
        D,M,Y;
    }

}
