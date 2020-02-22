package learning.BDD.utilities

import cucumber.api.java.After
import cucumber.api.java.Before

class HooksExample {

    @Before()
    public void setup() {
        println("Inside Generic Before Hook")
    }

    @After()
    public void tearDown() {
        println("Inside Generic After Hook")
    }

    @Before("@smoke,~@WIP")
    public void setuptag() {
        println("Inside Smoke Before Hook")
    }

    @After("@smoke,~@WIP")
    public void tearDowntag() {
        println("Inside Smoke After Hook")
    }

    @Before(order=0)
    public void setup0() {
        println("Inside Before 0 hook")
    }

    @After(order=0)
    public void tearDown0() {
        println("Inside After 0 hook")
    }

    @Before(order=1)
    public void setuptag1() {
        println("Inside Before 1 hook")
    }

    @After(order=1)
    public void tearDowntag1() {
        println("Inside After 1 hook")
    }


}

//order of hook , before will run lowest to highest, and after will run highest to lowest
