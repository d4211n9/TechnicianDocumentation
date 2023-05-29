package be;

import java.sql.Timestamp;

public class DeviceLogin {
    private int ID;
    private String username, password;
    private Timestamp deleted;
    private Device device;

    public DeviceLogin(Device device, String username, String password) {
        this.device = device;
        this.username = username;
        this.password = password;
    }

    public DeviceLogin(int ID, Device device, String username, String password) {
        this.ID = ID;
        this.device = device;
        this.username = username;
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public Device getDevice() {
        return device;
    }

    public String getDeviceTypeName() {
        return device.getDeviceTypeName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setDeleted(Timestamp deleted) {
        this.deleted = deleted;
    }
}
