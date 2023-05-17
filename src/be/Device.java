package be;

public class Device {
    private DeviceType deviceType;
    private double posX, posY, width, height;

    public Device() {

    }

    public Device(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
