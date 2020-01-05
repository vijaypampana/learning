package learning.design.creational.factory

class GenerateBill {

    public static void main(String[] args) {

        GetPlanFactory GPF = new GetPlanFactory();
        println("Enter the name of plan for which the bill will be generated")
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

        String planName = br.readLine();
        println("Enter the number of units for bill will be calculated")
        Integer units = Integer.parseInt(br.readLine())

        Plan p = GPF.getPlan(planName)   //null is handled

        println("Bill Amount for " +planName + " of " + units + " units is : " + p.calculateBill(units))
    }
}
