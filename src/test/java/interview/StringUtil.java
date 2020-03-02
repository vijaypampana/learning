package interview;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class StringUtil {

    @Test
    String reverse(String val) {
        return StringUtils.reverse(val);
    }

    @Test
    String reverseTheWords(String val, Character seperator) {
        return StringUtils.reverseDelimited(val, seperator);
    }

    @Test
        //<T> is needed even the method is returning void and also T extends super otherwise sort will not work, it assert that T is a type which can be comparable to itself
    <T extends Comparable<? super  T>> void missingArrayElement(List<T> val1, List<T> val2) {
        Collections.sort(val1);
        Collections.sort(val2);
        (val1.size() > val2.size() ? val1 : val2).forEach(val -> {
            if(!(val1.size() > val2.size() ? val2 : val1).contains(val)) {

                System.out.println(val);
            }
        });
    }

    @Test
    <T> List<T> arrayToList(T[] array) {
        return Arrays.stream(array).collect(Collectors.toList());
    }

    @Test
        //Example for multiple Generic values
    <T,G> List<G> arrayToList(T[] a, Function<T, G> mapperFunction) {
        return Arrays.stream(a).map(mapperFunction).collect(Collectors.toList());
    }

    @Test
        //Example for bounded Generic
    <T extends Number & Comparable<? super T>> List<Integer> boundedGeneric(T[] a) {
        return Arrays.stream(a).map(T::intValue).collect(Collectors.toList());
    }




}
