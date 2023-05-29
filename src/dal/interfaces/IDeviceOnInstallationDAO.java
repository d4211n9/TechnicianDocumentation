package dal.interfaces;

import be.Device;

import java.util.List;

public interface IDeviceOnInstallationDAO {
    boolean addDeviceToInstallation(Device device, int installationID) throws Exception;
    List<Device> getDevicesFromInstallation(int installationID) throws Exception;
    boolean removeDevicesFromInstallation(int installationID) throws Exception;

}
