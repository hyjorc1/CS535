package PA3;

/**
 * Tuple
 */
public class Tuple {

    private String edge;
    private double weight;

    public Tuple(String edge, double weight) {
        this.edge = edge;
        this.weight = weight;
    }

    public String getEdge() {
        return this.edge;
    }

    public String getPage() {
        return this.edge.split(" ")[0];
    }

    public String getLink() {
        return this.edge.split(" ")[1];
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "<(" + this.edge + "), " + weight + ">";
    }

}