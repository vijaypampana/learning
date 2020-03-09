package learning.java8.Functional;

import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SupplierExample {

    @Test
    public void testSupplier() {

        Supplier<Calendar> calendarSupplier = new Supplier<Calendar>() {
            @Override
            public Calendar get() {
                return Calendar.getInstance();
            }
        };

        Calendar c = calendarSupplier.get();
    }

    @Test
    public void testConsumer() {

        Supplier<Date> dateSupplier = Date::new;
        Consumer<Date> oneDayIncrement = (date1) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy EEEE"); //dd/MMM/yyyy
            System.out.println(sdf.format(date1));
        };

        oneDayIncrement.accept(dateSupplier.get());
    }
}

//Supplier
// <T> get()
//Consumer
// accept(T t)
// andThen(Consumer otherConsumer)