package learning.design.creational.abstractPattern

class FactoryCreator_4 {

    public static AbstractFactory_2 getFactory(String choice) {
        if("bank".equalsIgnoreCase(choice)) {
            return new BankFactory_3()
        } else if ("loan".equalsIgnoreCase(choice)) {
            return new LoanFactory_3()
        } else {
            return null;
        }
    }
}
