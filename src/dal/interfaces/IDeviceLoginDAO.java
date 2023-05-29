package dal.interfaces;

import be.Device;
import be.DeviceLogin;

public interface IDeviceLoginDAO {
    DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception;
    DeviceLogin getDeviceLogin(Device device) throws Exception;
    DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception;
}
