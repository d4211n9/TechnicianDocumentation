package dal.interfaces;

import be.Device;

import java.util.List;

public interface IDeviceDAO {
    List<Device> getAllDevicesFromDrawingId(int drawingId) throws Exception;
    List<Device> createDevices(List<Device> devicesToCreate, int drawingId) throws Exception;
}
