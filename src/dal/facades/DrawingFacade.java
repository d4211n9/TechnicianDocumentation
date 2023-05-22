package dal.facades;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;
import dal.dao.DeviceDAO;
import dal.dao.DeviceLoginDAO;
import dal.dao.DeviceOnInstallationDAO;
import dal.dao.DeviceTypeDAO;
import dal.interfaces.IDeviceDAO;
import dal.interfaces.IDeviceLoginDAO;
import dal.interfaces.IDeviceOnInstallationDAO;
import dal.interfaces.IDeviceTypeDAO;

import java.util.List;

public class DrawingFacade {
    private IDeviceTypeDAO deviceTypeDAO;
    private IDeviceDAO deviceDAO;
    private IDeviceLoginDAO deviceLoginDAO;
    private IDeviceOnInstallationDAO deviceOnInstallationDAO;

    public DrawingFacade() throws Exception {
        deviceTypeDAO = new DeviceTypeDAO();
        deviceDAO = new DeviceDAO();
        deviceLoginDAO = new DeviceLoginDAO();
        deviceOnInstallationDAO = new DeviceOnInstallationDAO();
    }

    public List<DeviceType> getAllDeviceTypes() throws Exception {
        return deviceTypeDAO.getAllDeviceTypes();
    }

    public boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception {
        return deviceTypeDAO.createDeviceType(deviceTypeToCreate);
    }

    public DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        return deviceLoginDAO.createDeviceLogin(deviceLogin);
    }

    public DeviceLogin getDeviceLogin(Device device) throws Exception {
        return deviceLoginDAO.getDeviceLogin(device);
    }

    public DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        return deviceLoginDAO.updateDeviceLogin(deviceLogin);
    }

    public boolean addDeviceToInstallation(Device device, int installationID) throws Exception {
        Device newDevice = deviceDAO.createDevice(device);
        return deviceOnInstallationDAO.addDeviceToInstallation(newDevice, installationID);
    }

    public List<Device> getDevicesFromInstallation(int installationID) throws Exception {
        return deviceOnInstallationDAO.getDevicesFromInstallation(installationID);
    }

    public boolean removeDevicesFromInstallation(int installationID) throws Exception {
        return deviceOnInstallationDAO.removeDevicesFromInstallation(installationID);
    }
}
