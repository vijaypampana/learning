package learning.design.creational.abstractPattern

class ICICI_1 implements Bank_1 {

    public final String BNAME;

    public ICICI_1() {
        BNAME = "HDFC BANK"
    }

    public String getBankName() {
        return BNAME;
    }
}
