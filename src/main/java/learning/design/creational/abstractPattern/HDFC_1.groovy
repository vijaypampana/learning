package learning.design.creational.abstractPattern

class HDFC_1 implements Bank_1 {

    public final String BNAME;

    public HDFC_1() {
        BNAME = "HDFC BANK"
    }

    public String getBankName() {
        return BNAME;
    }
}
