package dal.facades;

import be.*;
import dal.dao.*;
import dal.interfaces.*;
import exceptions.DALException;

import java.util.List;

public class DrawingFacade {
    private IDeviceTypeDAO deviceTypeDAO;
    private IDeviceDAO deviceDAO;
    private IDeviceLoginDAO deviceLoginDAO;
    private IDeviceOnInstallationDAO deviceOnInstallationDAO;

    private IWireTypeDAO wireTypeDAO;

    private IWireDAO wireDAO;

    private IWiresOnInstallationDAO wiresOnInstallationDAO;


    public DrawingFacade() throws Exception {
        deviceTypeDAO = new DeviceTypeDAO();
        deviceDAO = new DeviceDAO();
        deviceLoginDAO = new DeviceLoginDAO();
        deviceOnInstallationDAO = new DeviceOnInstallationDAO();

        wireTypeDAO = new WireTypeDAO();
        wireDAO = new WireDAO();
        wiresOnInstallationDAO = new WiresOnInstallation();

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

    public boolean removeWiresFromInstallation(int id) throws DALException {
        return wiresOnInstallationDAO.removeWiresFromInstallation(id);
    }

    public boolean addWireToInstallation(Wire wire, int id) throws DALException {
        Wire newWire = wireDAO.createWire(wire);
        return wiresOnInstallationDAO.addWireToInstallation(newWire, id);
    }

    public List<Wire> getWiresFromInstallation(int id) throws DALException {
        return wiresOnInstallationDAO.getWiresFromInstallation(id);
    }
}
