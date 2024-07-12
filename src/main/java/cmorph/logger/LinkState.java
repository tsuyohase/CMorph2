package cmorph.logger;

public class LinkState {
    private double load;

    public LinkState() {
        this.load = 0.0;
    }

    public LinkState(double load) {
        this.load = load;
    }

    public double getLoad() {
        return this.load;
    }

}
