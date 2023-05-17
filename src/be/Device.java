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

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
