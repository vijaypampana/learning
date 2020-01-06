package learning.design.creational.abstractPattern

abstract class Loan_1 {

    protected double rate;
    abstract void getInterestRate(double rate);
    public void calculateLoanPayment(double loanAmount, int years) {
        int n = years * 12
        rate = rate/1200;
        double EMI = ((rate*Math.pow((1+rate),n))/((Math.pow((1+rate),n))-1))*loanAmount
        println("Your monthly EMI is $EMI for the amount $loanAmount you have borrowed")
    }
}
