package bll.interfaces;

import be.DeviceType;
import be.Drawing;

import java.util.List;

public interface IDrawingManager {
    Drawing getDrawingFromInstallationId(int installationId) throws Exception;
    List<DeviceType> getAllDeviceTypes() throws Exception;
    boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception;

    void deleteDrawing(Drawing drawing) throws Exception;
}
