package dal.facades;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;
import be.WireType;
import dal.dao.*;
import dal.interfaces.*;

import java.util.List;

public class DrawingFacade {
    private IDeviceTypeDAO deviceTypeDAO;
    private IDeviceDAO deviceDAO;
    private IDeviceLoginDAO deviceLoginDAO;
    private IDeviceOnInstallationDAO deviceOnInstallationDAO;

    private IWireTypeDAO wireTypeDAO;

    public DrawingFacade() throws Exception {
        deviceTypeDAO = new DeviceTypeDAO();
        deviceDAO = new DeviceDAO();
        deviceLoginDAO = new DeviceLoginDAO();
        deviceOnInstallationDAO = new DeviceOnInstallationDAO();
        wireTypeDAO = new WireTypeDAO();
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

    public boolean createWireType(WireType wireType) throws Exception {
        return wireTypeDAO.createWireType(wireType);
    }

    public List<WireType> getAllWireTypes() throws Exception {

        return wireTypeDAO.getAllWireTypes();
    }
}
