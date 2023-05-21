package bll.managers;

import be.Device;
import be.DeviceType;
import be.Drawing;
import bll.interfaces.IDrawingManager;
import dal.facades.DrawingFacade;

import java.util.List;

public class DrawingManager implements IDrawingManager {
    private DrawingFacade drawingFacade;

    public DrawingManager() throws Exception {
        drawingFacade = new DrawingFacade();
    }

    @Override
    public Drawing getDrawingFromInstallationId(int installationId) throws Exception {
        return drawingFacade.getDrawingFromInstallationId(installationId);
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
    public List<Device> createDevices(List<Device> devicesToCreate, int drawingId) throws Exception {
        return drawingFacade.createDevices(devicesToCreate, drawingId);
    }
}
