package learning.java8.Functional;

import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaExpressions {

    @Test
    public void predicateLambda() {
        Predicate<String> testPL = (String str) -> str.contains("Vijay");
        System.out.println(testPL.test("Rahul Vijay Gandhi"));
    }

    //Supplier dont have any input so for Lambda we put empty braces ()
    @Test
    public void testSupplier() {
        Supplier<Calendar> calendarSupplier = () -> Calendar.getInstance();
        System.out.println(calendarSupplier.get().getTime());
    }

}

//if you are not using return in lambda expression dont use the curly braces
