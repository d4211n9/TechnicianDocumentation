package dal.facades;

import be.Device;
import be.DeviceType;
import be.Drawing;
import dal.dao.DeviceDAO;
import dal.dao.DeviceTypeDAO;
import dal.dao.DrawingDAO;
import dal.interfaces.IDeviceDAO;
import dal.interfaces.IDeviceTypeDAO;
import dal.interfaces.IDrawingDAO;

import java.util.List;

public class DrawingFacade {
    private IDeviceTypeDAO deviceTypeDAO;
    private IDeviceDAO deviceDAO;
    private IDrawingDAO drawingDAO;

    public DrawingFacade() throws Exception {
        deviceTypeDAO = new DeviceTypeDAO();
        drawingDAO = new DrawingDAO();
        deviceDAO = new DeviceDAO();
    }


    public Drawing createDrawing(Drawing drawing) throws Exception {
        return drawingDAO.createDrawing(drawing);
    }

    public Drawing getDrawingFromInstallationId(int installationId) throws Exception {
        Drawing drawing = drawingDAO.getDrawingFromInstallationId(installationId);
        drawing.getDevices().addAll(deviceDAO.getAllDevicesFromDrawingId(drawing.getId()));

        return drawing;
    }

    public List<DeviceType> getAllDeviceTypes() throws Exception {
        return deviceTypeDAO.getAllDeviceTypes();
    }

    public boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception {
        return deviceTypeDAO.createDeviceType(deviceTypeToCreate);
    }

    public List<Device> createDevices(List<Device> devicesToCreate, int drawingId) throws Exception {
        return deviceDAO.createDevices(devicesToCreate, drawingId);
    }

    public void deleteDrawing(Drawing drawing) throws Exception {
        drawingDAO.deleteDrawing(drawing);
    }
}
