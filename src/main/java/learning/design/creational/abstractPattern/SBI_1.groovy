package learning.design.creational.abstractPattern

class SBI_1 implements Bank_1 {

    public final String BNAME;

    public SBI_1() {
        BNAME = "ICICI BANK"
    }

    public String getBankName() {
        return BNAME;
    }
}
