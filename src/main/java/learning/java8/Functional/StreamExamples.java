package learning.java8.Functional;

import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExamples {

    @Test(enabled = false)
    public void testStream() {

        //Creating a stream from scratch
        Stream<Integer> intStream1 = Stream.of(3,4,5,6,7,9);
        //2 is included and 10 is excluded
        IntStream intStream2 = IntStream.range(2,10);       //We can use Double Stream, LongStream and range() method on them
        //10 is included and 50 is excluded but we skip first 5 items 10 to 14 are skipped
        IntStream intStream3 = IntStream.range(10,50).skip(5); //Skip first 5 items
        intStream1.forEach(i -> System.out.println(i));
        intStream2.forEach(i -> System.out.println(i));
        intStream3.forEach(System.out::println);

        Stream<Double> doubleStream1 = Stream.of(2.5, 3.6);
        DoubleStream doubleStream2 = DoubleStream.of(2.5, 3.6);

    }

    @Test
    void collectionToStream() {

        List<String> strList = new ArrayList<>();
        strList.add("Vijay");
        strList.add("Sanvi");
        strList.add("Deepu");
        strList.add("Sachin");

        //strList.forEach(System.out::println);
        //Converting an Arraylist to stream
        Stream<String> strStream = strList.stream();
        //sorted to sort the values
        strStream.sorted().filter(s -> s.startsWith("S")).forEach(System.out::println);

    }

    @Test (enabled = false)
    void testFilterStream() {

        List<Player> players = new ArrayList<>();
        players.add(new Player(123, 10, 6, 2));
        players.add(new Player(234, 8, 4, 1));
        players.add(new Player(567, 6, 6, 0));
        players.add(new Player(890, 2, 4, 0));

        Stream<Player> playerStream = players.stream();
        playerStream.forEach(t -> System.out.println(t.playerId));

        System.out.println("---------------------------------");
        //we cannot reuse the stream as it will throw the below error message
        //java.lang.IllegalStateException: stream has already been operated upon or closed
        playerStream = players.stream();
        playerStream
                .filter(p -> p.playerMatch > 5)
                .filter(p -> p.fouls == 0)
                .forEach(t -> System.out.println(t.playerId));


    }

    class Player {

        private Integer playerId;
        private Integer playerMatch;
        private Integer playGoals;
        private Integer fouls;

        public Player(Integer id, Integer matches, Integer goals, Integer fouls) {
            this.playerId = id;
            this.playerMatch = matches;
            this.playGoals = goals;
            this.fouls = fouls;
        }
    }

    @Test (enabled = false)
    void testMapFunction() {

        List<String> dateList = new ArrayList();
        dateList.add("01/01/2020");
        dateList.add("02/02/2020");
        dateList.add("03/03/2020");
        dateList.add("04/04/2020");

        //Map will take a function as we are using static function we will refer it with Class name
        dateList.stream()
                .map(StreamExamples::convertDate)
                .filter(d -> !StreamExamples.isWeekend(d))
                .forEach(System.out::println);

    }

    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static Date convertDate(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    void testReduce() {

        List<Integer> radius = new ArrayList<>();
        radius.add(3);
        radius.add(5);
        radius.add(7);
        radius.add(5);

        Stream<Integer> radiusStream = radius.stream();
        radiusStream.collect(Collectors.toCollection(TreeSet::new));


        List<Employee> employeesList = new ArrayList<>();
        employeesList.add(new Employee(1, 10000, "Vijay", "QEA"));
        employeesList.add(new Employee(2, 7000, "Rahul", "SA"));
        employeesList.add(new Employee(3, 6000, "Shyam", "SA"));
        employeesList.add(new Employee(4, 4000, "Mohan", "QEA"));
        employeesList.add(new Employee(5, 10000, "Vaidyanathan", "DEV"));
        employeesList.add(new Employee(6, 5000, "Lokesh", "Admin"));
        employeesList.add(new Employee(7, 8000, "Shymala", "HR"));

        Stream<Employee> employeeStream = employeesList.stream();
        System.out.println(employeeStream.collect(Collectors.summingInt(t -> t.getSalary())));
        //employeesList.stream().map(t -> t.getDept()).distinct().sorted().forEach(System.out::println);
        List<String> deptList = employeesList.stream().map(t -> t.getDept()).distinct().sorted().collect(Collectors.toList());
        HashSet<String> deptHashSet = employeesList.stream().map(t -> t.getDept()).collect(Collectors.toCollection(HashSet::new));

    }

    class Employee {

        private Integer id;
        private Integer salary;
        private String dept;
        private String name;

        public Employee(Integer id, Integer salary, String name, String dept) {
            super();
            this.id = id;
            this.salary = salary;
            this.dept = dept;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public Integer getSalary() {
            return salary;
        }

        public String getDept() {
            return dept;
        }

        public String getName() {
            return name;
        }
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
//      Collectors.toList()                     :   Puts items into a list
//      Collectors.toCollection(TreeSet::new)   :   puts items in desired container
//      Collectors.joining(",")                 :   joins multiple items into a single item by concatenating them
//      Collectors.summingInt(item::getAge)     :   Sum the values of each item with given supplier
//      Collectors.groupingBy()                 :   Groups the items with given classifier and mapper
//      Collectors.partitioningBy()             :   Partitions the items with given predicate and mapper
