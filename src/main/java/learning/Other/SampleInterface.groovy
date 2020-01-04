package learning.Other

//An interface can extend only another interface only but cannot extend a abstract or java class
//Interface cannot be instantiated
//Are implemented by using keyword "implements"
//A java class can implement multiple interfaces
interface SampleInterface {

    //All variable in Interface by default final
    String msg;

    //All methods are public by default and cannot have any implementation
    public void sendMsg()

}