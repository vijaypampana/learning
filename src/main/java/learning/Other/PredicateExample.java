package learning.Other;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class PredicateExample {

    public static void main(String[] args) {

        Person p1 = new Person(36, "Vijay");
        Person p2 = new Person(26, "Ram");
        Person p3 = new Person(38, "Krishna");
        Person p4 = new Person(22, "Allu");

        List<Person> personList = Arrays.asList(p1,p2,p3,p4);
        List<Person> filteredList = getAgeGT30(personList, personAge30);
        System.out.println("test");
    }

    //The same example below can be written in a Lambda expression
//    public static Predicate<Person> personAge30 = new Predicate<Person>() {
//        @Override
//        public boolean test(Person person) {
//           return person.getAge() > 30;
//        }
//    };

    //Lambda expression in one line for above example
    public static Predicate<Person> personAge30 = p1 -> p1.getAge() > 30;

    public static List<Person> getAgeGT30(List<Person> personList, Predicate<Person> personPredicate) {
        List<Person> personList1 = new ArrayList<>();
        for(Person p1 : personList) {
            if(personPredicate.test(p1)) {
                personList1.add(p1);
            }
        }
        return personList1;
    };



}

class Person {
    int age;
    String name;
    Person(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }
}


//Predicate is a functional interface with abstract method test(Object) and returns boolean and take one parameter. Can be used in Lambda expressions
// Syntax predicate<Type> p = condition