package learning.java8.Functional;

import org.testng.annotations.Test;

//It is a container and get method
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class OptionalTest {

    @Test(enabled = false)
    public void testOptional1() {
        Optional<String> optionalString; // = Optional.empty();
        //System.out.println(optionalString);
        String str2 = null;

        //Avoided null pointer exception using ofNullable
        optionalString = Optional.ofNullable(str2);

        if(optionalString.isPresent()) {
            System.out.println(optionalString.get().toUpperCase());
        }

        //Or else method will take the value when it is null
        System.out.println(optionalString.orElse("Java 8").toUpperCase());
    }

    @Test
    public void testOptional2() {

        //This method will return a second word if present otherwise a null value
        //Function take Input as a string and output as a String
        Function<String, String> getSecondWord = (String s) -> {
            return s.split(" ").length > 1 ? s.split(" ")[1] : null;
        };

        //This function takes a string input and return the length of that String
        Function<String, Integer> getLetterCount = t -> t.length();

        String testWord = "Vijay";

        //ofNullable will return empty when it is null otherwise actual object
        Optional<String> testOptional = Optional.ofNullable(getSecondWord.apply(testWord));
        //If present example where it uses a consumer function (system.out::println)
        testOptional.ifPresent(System.out::println);
        //orElse will return Great when testOptional is empty
        System.out.println(getLetterCount.apply(testOptional.orElse("Great")));
        Optional.ofNullable(getSecondWord.apply(testWord)).ifPresent(System.out::println);
        //Chain of functions (Map takes a function as input)
        Optional.ofNullable(getSecondWord.apply(testWord)).map(getLetterCount).ifPresent(System.out::println);
    }

    @Test
    public void readFirst10Error() throws IOException {
        List<String> errors = Files.lines(Paths.get("C:\\dev\\VS\\bdd\\src\\main\\java\\learning\\java8\\Functional\\logFile.log")).filter(l -> l.startsWith("ERROR")).limit(10).collect(toList());
        System.out.println("test");
    }


}

// Optional
// ofNullable : gives a empty when null otherwise the value
// isPresent() : Boolean eg; if(testOptional.isPresent())
// ifPresent(Consumer c) eg: ifPresent(System.out::println)
// getOrElse(Supplier s)

