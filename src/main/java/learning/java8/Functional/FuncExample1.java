package learning.java8.Functional;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class FuncExample1 {

    @Test
    public void readFirst40Error() throws IOException {
        List<String> errors = Files.lines(Paths.get("src/main/java/learning/java8/Functional/logFile.log")).filter(l -> l.startsWith("ERROR")).limit(10).collect(toList());
        System.out.println("test");
        //Collections.unModifiableMAP/Array/List
    }

    @Test
    void functionTest() {

        List<String> strList = new ArrayList<>();
        strList.add("Vijay");
        strList.add("Sanvika");
        strList.add("Jajulu");

        Function<String, String> changeCase = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.toUpperCase();
            }
        };

        Function<String, Integer> findWordCount = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.split(" ").length;
            }
        };

        //apply is where the logic resides
        //andThen is used to apply first existing logic and then the logic present in andThen Function
        //compose is used to apply changeCase first and then findWordCount. Viceversa to Above
        //Iterator
        String longString = "Vijay is a good boy trying to learn Java 8";
        System.out.println(changeCase.apply(longString));
        System.out.println(findWordCount.apply(longString));
        System.out.println(changeCase.andThen(findWordCount).apply(longString));
        System.out.println(findWordCount.compose(changeCase).apply(longString));
        //System.out.println()

    }


    @Test
    void dateFunction() {

        //Supplier Function giving the Current Date
        Supplier<Date> todayDate = Date::new;
        Date dateToday = todayDate.get();

        //Creating a instance of the MyDate class
        //As we are referring to static method or instance we are using Class name before ::
        Supplier<MyDate> myDate = MyDate::new;
        MyDate myDateInstance = myDate.get();

        //When referring to non static methods are construction then we need to refer it by an instance
        Supplier<Date> myDateTommorrow = myDateInstance :: getTomorrow;

        Function<Date, String> dayPrinter = (date -> { return (new SimpleDateFormat("EEEE")).format(date);});
        System.out.println(dayPrinter.apply(dateToday));

        Predicate<Date> weekPredicate = myDateInstance::isWeekend;
        if(weekPredicate.test(new Date()))
            System.out.println("Today is Weekend");;
    }

}


//Function      : Single Input and Single Output
//BiFunction    : Two arguments as inputs, one Object as output
//Predicate     : Single Object as input and Output as a Boolean
//BiPredicate   : Two objects as arguments, and output as a boolean
//Consumer      : One object as input, output None, example writing content of a object to file or console
//Supplier      : None input, output one object, like creating a new object
