package learning.design.creational.factory

abstract class Plan {

    protected double rate;

    abstract void getRate();

    public void calculateBill(int Units) {
        println(Units * rate)
    }
}
