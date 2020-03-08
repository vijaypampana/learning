package learning.java8.Functional;

import cucumber.api.java8.Ca;

import java.util.Calendar;
import java.util.Date;

public class MyDate {

    public static Date today;
    private Date tomorrow, yesterday;
    private Calendar calendar = Calendar.getInstance();

    public MyDate() {
        today = new Date();
        tomorrow = getTomorrow(today);
        yesterday = getYesterday(today);
    }

    public Date getTomorrow(Date date1) {
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public Date getYesterday(Date date1) {
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public Date getTomorrow() {
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public Boolean isWeekend(Date date1) {
        calendar.setTime(date1);
        return ((Calendar.DAY_OF_WEEK == Calendar.SATURDAY) || (Calendar.DAY_OF_WEEK == Calendar.SUNDAY));
    }
}
