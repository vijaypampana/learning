package learning.design.creational.abstractPattern

class BankFactory_3 extends AbstractFactory_2 {

    public Bank_1 getBank(String bank) {

        if(bank == null){
            return null;
        }
        if(bank.equalsIgnoreCase("HDFC")){
            return new HDFC_1();
        } else if(bank.equalsIgnoreCase("ICICI")){
            return new ICICI_1();
        } else if(bank.equalsIgnoreCase("SBI")){
            return new SBI_1();
        }
        return null;

    }

    public Loan_1 getLoan(String loan) {
        return null;
    }

}
