package dal.facades;

import be.DeviceType;
import be.Drawing;
import dal.dao.DeviceTypeDAO;
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
    }

    public List<DeviceType> getAllDeviceTypes() throws Exception {
        return deviceTypeDAO.getAllDeviceTypes();
    }

    public boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception {
        return deviceTypeDAO.createDeviceType(deviceTypeToCreate);
    }
}
