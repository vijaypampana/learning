package interview;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainTest {

    public static void main(String[] args) {
        StringUtil stringUtil = new StringUtil();
        FileUtils fileUtils = new FileUtils();
        String str = "Vijay is a good boy";
        Integer[] intArray = {1,2,4,0,3};
        //Arrays.asList(intArray).remove()

        Scanner scanner = new Scanner(System.in);
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the String:");
        //String temp = scanner.nextLine();
        //String temp = br.readLine();
        //System.out.println (temp);

//        List<String> stringList = Arrays.asList(str.split(" "));
//        Character char1 = ' ';
//        System.out.println(str.split(" "));
//        System.out.println(stringUtil.reverse(str));
//        System.out.println(stringUtil.reverseTheWords(str, char1));
//        stringUtil.missingArrayElement(Arrays.asList('1','2','3','4'), Arrays.asList('4','1','3'));
//        System.out.println(stringUtil.arrayToList(str.split(" ")));
//        System.out.println(stringUtil.arrayToList(str.split(" "), String::toUpperCase));
        System.out.println(fileUtils.getnThMinInt(Arrays.asList(intArray), 1));

    }
}
