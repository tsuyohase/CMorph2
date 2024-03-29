package cmorph.utils;

public class Point {
    private double x;
    private double y;

    public Point() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getDistance(Point point) {
        double xDistance = this.x - point.getX();
        double yDistance = this.y - point.getY();
        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }
}
