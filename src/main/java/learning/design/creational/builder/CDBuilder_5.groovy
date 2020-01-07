package learning.design.creational.builder

class CDBuilder_5 {

    public CDType_4 buildSonyCD() {
        CDType_4 cds = new CDType_4()
        cds.addItem(new SONY_3())
        return cds
    }

    public CDType_4 buildSamsungCD() {
        CDType_4 cds = new CDType_4()
        cds.addItem(new SAMSUNG_3())
        return cds
    }

}
