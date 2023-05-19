package dal.facades;

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

    public void deleteDrawing(Drawing drawing) throws Exception {
        drawingDAO.deleteDrawing(drawing);
    }
}