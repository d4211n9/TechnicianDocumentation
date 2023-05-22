package bll.interfaces;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;

import java.util.List;

public interface IDrawingManager {
    List<DeviceType> getAllDeviceTypes() throws Exception;
    boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception;
    DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception;
    DeviceLogin getDeviceLogin(Device device) throws Exception;
    DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception;
    boolean addDeviceToInstallation(Device device, int installationID) throws Exception;
    List<Device> getDevicesFromInstallation(int installationID) throws Exception;
    boolean removeDevicesFromInstallation(int installationID) throws Exception;
}
