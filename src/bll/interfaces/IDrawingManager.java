package bll.interfaces;

import be.*;
import exceptions.DALException;

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

    boolean createWireType(WireType wireType) throws Exception;

    List<WireType> getAllWireTypes() throws Exception;

    boolean removeWireFromInstallation(int id) throws DALException;

    boolean addWireToInstallation(Wire wire, int id) throws DALException;

    List<Wire>  getWiresFromInstallation(int id) throws DALException;
}
