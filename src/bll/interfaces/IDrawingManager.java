package bll.interfaces;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;
import be.Drawing;

import java.util.List;

public interface IDrawingManager {

    Drawing createDrawing(Drawing drawing) throws Exception;
    Drawing getDrawingFromInstallationId(int installationId) throws Exception;
    List<DeviceType> getAllDeviceTypes() throws Exception;
    boolean createDeviceType(DeviceType deviceTypeToCreate) throws Exception;
    List<Device> createDevices(List<Device> devicesToCreate, int drawingId) throws Exception;
    void deleteDrawing(Drawing drawing) throws Exception;
    DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception;
    DeviceLogin getDeviceLogin(Device device) throws Exception;
    DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception;
}
