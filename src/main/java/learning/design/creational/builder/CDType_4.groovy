package learning.design.creational.builder


class CDType_4 {

    private List<Packing_1> items = new ArrayList<>()

    public void addItem(Packing_1 pac) {
        items.add(pac)
    }

    public void getCost() {
        items.each { val ->
            val.price()
        }
    }

    public void showItems() {
        items.each { val ->
            println(val.price())
            println(val.pack())
        }
    }

}
