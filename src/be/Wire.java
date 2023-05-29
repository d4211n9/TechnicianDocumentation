package be;


public class Wire {
    private int id;
    private double startX, startY, endX, endY;

    private WireType wireType;



    public Wire(int id, double startX, double startY, double endX, double endY, WireType wireType) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.wireType = wireType;
    }

    public Wire(double startX, double startY, double endX, double endY, WireType wireType) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.wireType = wireType;
    }

    public int getId() {
        return id;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WireType getWireType() {
        return wireType;
    }

    public void setWireType(WireType wireType) {
        this.wireType = wireType;
    }
}
