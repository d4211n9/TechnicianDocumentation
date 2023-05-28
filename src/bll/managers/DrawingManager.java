package bll.managers;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;
import be.WireType;
import bll.interfaces.IDrawingManager;
import dal.facades.DrawingFacade;

import java.util.List;

public class DrawingManager implements IDrawingManager {
    private DrawingFacade drawingFacade;

    public DrawingManager() throws Exception {
        drawingFacade = new DrawingFacade();
    }

    @Override
    public List<DeviceType> getAllDeviceTypes() throws Exception {
        return drawingFacade.getAllDeviceTypes();
    }

    @Override
    public boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception {
        return drawingFacade.createDeviceType(deviceTypeToCreate);
    }

    @Override
    public DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        return drawingFacade.createDeviceLogin(deviceLogin);
    }

    @Override
    public DeviceLogin getDeviceLogin(Device device) throws Exception {
        return drawingFacade.getDeviceLogin(device);
    }

    @Override
    public DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        return drawingFacade.updateDeviceLogin(deviceLogin);
    }

    @Override
    public boolean addDeviceToInstallation(Device device, int installationID) throws Exception {
        return drawingFacade.addDeviceToInstallation(device, installationID);
    }

    @Override
    public List<Device> getDevicesFromInstallation(int installationID) throws Exception {
        return drawingFacade.getDevicesFromInstallation(installationID);
    }

    @Override
    public boolean removeDevicesFromInstallation(int installationID) throws Exception {
        return drawingFacade.removeDevicesFromInstallation(installationID);
    }

    @Override
    public boolean createWireType(WireType wireType) throws Exception {
        return drawingFacade.createWireType(wireType);
    }

    @Override
    public List<WireType> getAllWireTypes() throws Exception {
        return drawingFacade.getAllWireTypes();
    }
}
