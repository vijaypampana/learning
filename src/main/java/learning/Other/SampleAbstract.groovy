package learning.Other

abstract class SampleAbstract implements SampleInterface {

    //An abstract class can have public, private.. varibles
    private String name = "test";
    public String name1 = "test";
    protected String name2 = "test";
    static final String name3 = "test";

    //An abstract class can have method with partial implementation along with abstract method
    abstract void hello();

    @Override
    public void sendMsg() {
        System.out.println("Hello! Inside sendMsg method");
    }

    private String privateMethod() {
        return "Hello";
    }
}
