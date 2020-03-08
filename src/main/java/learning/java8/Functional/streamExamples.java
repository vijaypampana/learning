package learning.java8.Functional;

import org.testng.annotations.Test;

public class streamExamples {

    @Test
    public void testStream() {

    }

}

//Stream
//Intermediate and Termina1 operations
//Intermediate (returns stream)
// filter       : filter items according to a given predicate, Input predicate
// map          : process items and transforms, Input Function
// limit        : Limit the results, Input int
// sorted       : sort items inside stream, Comparator
// distinct     : remove duplicate items according to equals method of the given type
//Terminal Operations
// forEach      : For every item, outputs something, Input is Consumer
// count        : Count current items
// collect      : Reduces the stream into a desired collection
