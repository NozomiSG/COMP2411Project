public class Item {
    private final int id;
    private final String name;
    private final double weight;

    Item(int id, String name, double weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }
    public String getName() {
        return name;
    }
    public double getWeight() {
        return weight;
    }
}
