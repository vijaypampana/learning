package learning.design.creational.builder

class BuilderDemo_6 {

    public static void main (String[] args) {

        CDBuilder_5 cdBuilder5 = new CDBuilder_5()
        CDType_4 cdType41 = cdBuilder5.buildSonyCD()
        cdType41.showItems()

        CDType_4 cdType42 = cdBuilder5.buildSamsungCD()
        cdType42.showItems()
    }
}
