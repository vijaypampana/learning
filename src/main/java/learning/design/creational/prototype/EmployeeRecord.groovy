package learning.design.creational.prototype

class EmployeeRecord implements Prototype {

    @Override
    Prototype getClone() {

        return new EmployeeRecord()
    }

}