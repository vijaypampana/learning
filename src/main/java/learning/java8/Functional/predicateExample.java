package learning.java8.Functional;

import org.testng.annotations.Test;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class predicateExample {

    //This test shows predicate, test, AND, OR operations
    @Test
    public void testPredicate() {

        String testString = "Vijay is a good boy";
        Predicate<String> specialWordChecker = (str1) -> {return str1.contains("Vijay");};
        Predicate<String> stringSize = (str1) -> {return str1.length() > 10;};

        //Logical AND
        System.out.println(stringSize.and(specialWordChecker).test(testString));
        //Negating the StringSize predicate value
        System.out.println(stringSize.negate().and(specialWordChecker).test(testString));
        //Logical OR
        System.out.println(stringSize.or(specialWordChecker).test(testString));

        BiPredicate<List<String>, String> checkWhetherStringExists = new BiPredicate<List<String>, String>() {
            @Override
            public boolean test(List<String> strings, String s) {
                return strings.contains(s);
            }
        };

        BiPredicate<String, String> checkExpectedStringExists = (masterString, findString) -> {return  masterString.contains(findString);};

    }

}

//Predicate
// Boolean test()                           Tests if t conforms to a logic
// and (Predicate otherPredicate)           Logical AND Operation with another predicate
// or (Predicate otherPredicate)            Logical OR Operation with another predicate
// negate()                                 Logical not operation
