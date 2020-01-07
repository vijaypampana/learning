package learning.design.creational.prototype

class PrototypeDemo {

    public static void main(String[] args) {

        EmployeeRecord e1 = new EmployeeRecord()
        EmployeeRecord e2 = (EmployeeRecord) e1.getClone();

    }

}
