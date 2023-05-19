package be;

public class Device {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private DeviceType deviceType;
    private double posX, posY, width, height;

    public Device(int id) {
        this.id = id;
    }

    public Device(DeviceType deviceType, int id) {
        this.deviceType = deviceType;
        this.id = id;
    }

    public Device(int id, DeviceType deviceType, double posX, double posY, double width, double height) {
        this.id = id;
        this.deviceType = deviceType;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
