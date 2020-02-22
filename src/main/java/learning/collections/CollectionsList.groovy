package learning.collections

import org.testng.annotations.Test

class CollectionsList {

    @Test
    void test() {

        List<String> namesA = Arrays.asList("test", "Vijay", "Deepika", "Sanvika")
        List<String> namesAL = new ArrayList<>()
        List<String> namesLL = new LinkedList<>()

        println("ArrayList Size : ${namesA.size()}")
        println("Retrieving List Second data based on index : ${namesA.get(1)}")
        namesA.set(0, "testindex0")
        println("Printing the list namesA : $namesA")
        namesAL.add("Violin")
        namesAL.addAll("Piano", "Drum", "Guitar")
        println("Printing the Array List element ${namesAL.each {val -> print(val)}}")
        //namesAL.sort()
        Collections.sort(namesAL)
        namesAL.removeIf{val -> val.startsWith("Dr")}
        println(namesAL)
        Collections.sort(namesAL, Collections.reverseOrder())
        println(namesAL)

        namesLL.addAll("Rahul", "Hari", "Mohan")
        println(namesLL)

    }

}

//LL is good for manipulation of data but retrieval is slow. It uses double linked list to store elements
//AL is good for reading and storing data but manipulation is slow as all the bits are shifted in memory when an element is removed. It uses a dynamic Array
