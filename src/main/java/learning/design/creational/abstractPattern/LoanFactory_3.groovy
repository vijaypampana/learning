package learning.design.creational.abstractPattern

class LoanFactory_3 extends AbstractFactory_2 {

    public Loan_1 getLoan(String loan) {

        if(loan == null){
            return null;
        }
        if(loan.equalsIgnoreCase("Home")){
            return new HomeLoan_1();
        } else if(loan.equalsIgnoreCase("Business")){
            return new BusinessLoan_1();
        } else if(loan.equalsIgnoreCase("Educational")){
            return new EducationalLoan_1();
        }
        return null;

    }

    public Bank_1 getBank(String bank) {
        return null;
    }

}
