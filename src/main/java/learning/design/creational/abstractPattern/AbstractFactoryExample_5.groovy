package learning.design.creational.abstractPattern

class AbstractFactoryExample_5 {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        AbstractFactory_2 bankFactory = FactoryCreator_4.getFactory("Bank")
        AbstractFactory_2 loanFactory = FactoryCreator_4.getFactory("Loan")

        println("Entern the name of the bank")
        String bankName = br.readLine()
        println()
        println("Enter the type of loan")
        String loanType = br.readLine()

        Bank_1 b = bankFactory.getBank(bankName)
        Loan_1 l = loanFactory.getLoan(loanType)

    }

}
